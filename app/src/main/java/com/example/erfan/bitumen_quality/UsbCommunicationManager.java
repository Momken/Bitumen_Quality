package com.example.erfan.bitumen_quality;

/**
 * Created by Erfan on 24.07.2017.
 */


import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import android.widget.Toast;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
import android.util.Log;

public class UsbCommunicationManager  implements Runnable{

    static final String ACTION_USB_PERMISSION = "com.example.erfan.bitumen_quality";

    UsbManager usbManager;
    UsbDevice usbDevice = null;
    UsbInterface usbCdcInterface = null;
    UsbInterface usbHidInterface = null;
    UsbEndpoint usbCdcRead = null;
    UsbEndpoint usbCdcWrite = null;
    UsbDeviceConnection usbCdcConnection;
    Thread readThread = null;
    volatile boolean readThreadRunning = true;
    PendingIntent permissionIntent;
    Context context;

    StringBuilder resiveddata;

    byte[] readBytes = new byte[256];

    public UsbCommunicationManager(Context context)
    {
        this.context = context;
        usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);

      // ask permission from user to use the usb device
        permissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        context.registerReceiver(usbReceiver, filter);
    }

    public void connect()
    {
        Log.d("MainActivity", "Connect: Start");
        // check if there's a connected usb device
        if(usbManager.getDeviceList().isEmpty())
        {
            Log.d("MainActivity", "No connected devices");
            return;
        }

        // get the first (only) connected device
        Toast.makeText(context, "i got an USB connected", Toast.LENGTH_LONG).show();
        Log.d("MainActivity", "i got an USB connected");
        usbDevice = usbManager.getDeviceList().values().iterator().next();
        Log.d("MainActivity", usbDevice.toString());

        // user must approve of connection if not in the /res/usb_device_filter.xml file
        usbManager.requestPermission(usbDevice, permissionIntent);

        Log.d("MainActivity", "Connect: Ende");
    }

    public void stop()
    {
        usbDevice = null;
        usbCdcInterface = null;
        usbHidInterface = null;
        usbCdcRead = null;
        usbCdcWrite = null;

        context.unregisterReceiver(usbReceiver);
    }


    /*
        Wird wohl nicht verwendet ist aber zur sicherheit Programmiert
     */
    public String write(String data)
    {
        if(usbDevice == null)
        {
            return "no usb device selected";
        }

        int sentBytes = 0;
        if(!data.equals(""))
        {
            synchronized(this)
            {
                // send data to usb device
                byte[] bytes = data.getBytes();
                sentBytes = usbCdcConnection.bulkTransfer(usbCdcWrite, bytes, bytes.length, 1000);
            }
        }

        return Integer.toString(sentBytes);
    }

    public String read(StringBuilder dest)
    {
        resiveddata = dest;
        if(usbCdcRead == null)
        {
            Log.d("MainActivity", "not connected to a device");
            return "not connected to a device";

        }

        String state = "";

        if(readThread != null && readThread.isAlive())
        {
            readThreadRunning = false;
            state = "stopping usb listening thread";
        }
        else
        {
            readThreadRunning = true;
            readThread = new Thread(this);
            readThread.start();
            state = "starting usb listening thread";
          //  Toast.makeText(context, "starting usb listening thread", Toast.LENGTH_LONG).show();
        }

        Log.d("MainActivity", "read: "+ state);

        return state;

    }

    private void setupConnection()
    {

        // find the right interface
        for(int i = 0; i < usbDevice.getInterfaceCount(); i++)
        {     Log.d("MainActivity", "check device: "+ i);

            // communications device class (CDC) type device
            if(usbDevice.getInterface(i).getInterfaceClass() == UsbConstants.USB_CLASS_CDC_DATA)
            {
                usbCdcInterface = usbDevice.getInterface(i);

                // find the endpoints
                for(int j = 0; j < usbCdcInterface.getEndpointCount(); j++)
                {
                    Log.d("MainActivity", "check endpoint: " + j);
                    if(usbCdcInterface.getEndpoint(j).getType() == UsbConstants.USB_ENDPOINT_XFER_BULK)
                    {
                        if(usbCdcInterface.getEndpoint(j).getDirection() == UsbConstants.USB_DIR_OUT)
                        {
                            // from host to device
                            usbCdcWrite = usbCdcInterface.getEndpoint(j);
                            Log.d("MainActivity", "DIR_OUT");

                        }

                        if(usbCdcInterface.getEndpoint(j).getDirection() == UsbConstants.USB_DIR_IN)
                        {
                            // from device to host
                            usbCdcRead = usbCdcInterface.getEndpoint(j);
                            Log.d("MainActivity", "DIR_in:" + usbCdcRead.toString());
                        }
                    }
                }
            }
        }
    }

    private final BroadcastReceiver usbReceiver = new BroadcastReceiver()
    {
        public void onReceive(Context context, Intent intent)
        {
            Log.d("MainActivity", "im in BroadcastReceiver");

            String action = intent.getAction();
            if(ACTION_USB_PERMISSION.equals(action))
            {
                // broadcast is like an interrupt and works asynchronously with the class, it must be synced just in case
                synchronized(this)
                {
                    if(intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false))
                    {
                        // fetch all the endpoints
                        Log.d("MainActivity", "setupConnection");
                        setupConnection();

                        // open and claim interface
                        usbCdcConnection = usbManager.openDevice(usbDevice);
                        usbCdcConnection.claimInterface(usbCdcInterface, true);

                        // set dtr to true (ready to accept data)
                        usbCdcConnection.controlTransfer(0x21, 0x22, 0x1, 0, null, 0, 0);

                        // set flow control to 8N1 at 9600 baud
						/* int baudRate = 9600; byte stopBitsByte = 1; byte
						 * parityBitesByte = 0; byte dataBits = 8; byte[] msg =
						 * { (byte) (baudRate & 0xff), (byte) ((baudRate >> 8) &
						 * 0xff), (byte) ((baudRate >> 16) & 0xff), (byte)
						 * ((baudRate >> 24) & 0xff), stopBitsByte,
						 * parityBitesByte, (byte) dataBits }; */

                        //Log.d("trebla", "Flow: " + connection.controlTransfer(0x21, 0x20, 0, 0, new byte[] {(byte) 0x80, 0x25, 0x00, 0x00, 0x00, 0x00, 0x08}, 7, 0));

                        //connection.controlTransfer(0x21, 0x20, 0, 0, msg, msg.length, 5000);
                    }
                    else
                    {
                        Log.d("trebla", "Permission denied for USB device");
                    }
                }
            }
            else if(UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action))
            {
                if(usbDevice != null)
                {
                    usbCdcConnection.releaseInterface(usbCdcInterface);
                    usbCdcConnection.close();
                    usbCdcConnection = null;
                    usbDevice = null;
                    Log.d("MainActivity", "USB connection closed");
                }
            }
        }
    };

    @Override
    public void run()
    {
        Log.d("MainActivity", "Started the usb linstener");
        ByteBuffer buffer = ByteBuffer.allocate(255);
        UsbRequest request = new UsbRequest();
        request.initialize(usbCdcConnection, usbCdcRead);

      //  char dataByte, data ;
        int packetState = 0;

        while(readThreadRunning)
        {

         //   Log.d("MainActivity", " wait for a status event");
            // queue a request on the interrupt endpoint
            request.queue(buffer, buffer.capacity());
            // wait for status event
           //Log.d("MainActivity", " run.request: " + request.toString());

            if(usbCdcConnection.requestWait() == request)
            {
                // there is no way to know how many bytes are coming, so simply forward the non-null values

                for(int i = 0; i < buffer.capacity() && buffer.get(i) != 0 ; i++)
                {
                  //  Log.d("MainActivity", " run.request: " + "/" + (char)buffer.get(i) +"/" + " resiveddata: " + resiveddata.length() + " capacity:" + buffer.capacity());
                    resiveddata.append( (char) buffer.get(i));

                    if((char) buffer.get(i) == '\n')
                    {
                       // Toast toast = Toast.makeText(context, resiveddata.toString(), Toast.LENGTH_LONG);
                        //Log.d("MainActivity", "Hallo rabbit... " );

                       // toast.show();
                        Log.d("MainActivity", resiveddata.toString() );

                        resiveddata.delete(0, resiveddata.length()-1);

                        break;
                    }



                }

                if(packetState == 2)
                {
                    // send data to client
   /*                 Intent intent = new Intent();
                    intent.setAction("USB:result");
                    intent.putExtra("data", data);
                    context.sendBroadcast(intent);
                    Log.d("MainActivity", " run.request: " + data.toString());
                    resiveddata.append(data.toString());

                    // reset packet
                    packetState = 0;
                    resiveddata.delete(0,resiveddata.length());*/
                }
            }
            else
            {
                Log.e("MainActivity", "Was not able to read from USB device, ending listening thread");
                readThreadRunning = false;
                break;
            }
        }
    }
}
