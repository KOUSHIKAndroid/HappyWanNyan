package com.happywannyan.Utils;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.appcompat.BuildConfig;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.happywannyan.R;

import java.io.File;

import static java.security.AccessController.getContext;


/**
 * Created by su on 10/6/17.
 */

public class DownloaderAndShowFile {

    private static final String GOOGLE_DRIVE_PDF_READER_PREFIX = "http://drive.google.com/viewer?url=";
    private static final String HTML_MIME_TYPE = "text/html";

    /**
     * If a PDF reader is installed, download the PDF file and open it in a reader.
     * Otherwise ask the user if he/she wants to view it in the Google Drive online PDF reader.<br />
     * <br />
     * <b>BEWARE:</b> This method
     * @param context
     * @param pdfUrl
     * @return
     */
    public static void showPDFUrl( final Context context, final String pdfUrl ) {
        final String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(pdfUrl));
        Loger.MSG("mimeType",""+mimeType);
        if ( isPDFSupported( context ,mimeType) ) {
            downloadAndOpenPDF(context, pdfUrl);
        } else {
            askToOpenPDFThroughGoogleDrive( context, pdfUrl );
        }
    }

    /**
     * Downloads a PDF with the Android DownloadManager and opens it with an installed PDF reader app.
     * @param context
     * @param pdfUrl
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void downloadAndOpenPDF(final Context context, final String pdfUrl) {
        // Get filename
        final String filename = pdfUrl.substring( pdfUrl.lastIndexOf( "/" ) + 1 );

//        final String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(pdfUrl));
//
//        Loger.MSG("mimeType",""+mimeType);
        // The place where the downloaded PDF file will be put
        final File tempFile = new File( context.getExternalFilesDir( Environment.DIRECTORY_DOWNLOADS ), filename );
        if ( tempFile.exists() ) {
            // If we have downloaded the file before, just go ahead and show it.
            openPDF( context,tempFile);
            return;
        }

        // Show progress dialog while downloading
        final ProgressDialog progress = ProgressDialog.show( context, context.getString( R.string.pdf_show_local_progress_title ), context.getString( R.string.pdf_show_local_progress_content ), true );

        // Create the download request
        DownloadManager.Request r = new DownloadManager.Request( Uri.parse( pdfUrl ) );
        r.setDestinationInExternalFilesDir( context, Environment.DIRECTORY_DOWNLOADS, filename );
        final DownloadManager dm = (DownloadManager) context.getSystemService( Context.DOWNLOAD_SERVICE );
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if ( !progress.isShowing() ) {
                    return;
                }
                context.unregisterReceiver( this );

                progress.dismiss();
                long downloadId = intent.getLongExtra( DownloadManager.EXTRA_DOWNLOAD_ID, -1 );
                Cursor c = dm.query( new DownloadManager.Query().setFilterById( downloadId ) );

                if ( c.moveToFirst() ) {
                    int status = c.getInt( c.getColumnIndex( DownloadManager.COLUMN_STATUS ) );
                    if ( status == DownloadManager.STATUS_SUCCESSFUL ) {
                        openPDF( context,tempFile);
                    }
                }
                c.close();
            }
        };
        context.registerReceiver( onComplete, new IntentFilter( DownloadManager.ACTION_DOWNLOAD_COMPLETE ) );

        // Enqueue the request
        dm.enqueue( r );
    }

    /**
     * Show a dialog asking the user if he wants to open the PDF through Google Drive
     * @param context
     * @param pdfUrl
     */
    public static void askToOpenPDFThroughGoogleDrive( final Context context, final String pdfUrl ) {
        new AlertDialog.Builder( context )
                .setTitle( R.string.pdf_show_online_dialog_title )
                .setMessage( R.string.pdf_show_online_dialog_question )
                .setNegativeButton( R.string.no, null )
                .setPositiveButton( R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openPDFThroughGoogleDrive(context, pdfUrl);
                    }
                })
                .show();
    }

    /**
     * Launches a browser to view the PDF through Google Drive
     * @param context
     * @param pdfUrl
     */
    public static void openPDFThroughGoogleDrive(final Context context, final String pdfUrl) {
        Intent i = new Intent( Intent.ACTION_VIEW );
        i.setDataAndType(Uri.parse(GOOGLE_DRIVE_PDF_READER_PREFIX + pdfUrl ), HTML_MIME_TYPE );
        context.startActivity( i );
    }
    /**
     * Open a local PDF file with an installed reader
     * @param context
     * @param tempFile
     */
    public static final void openPDF(Context context,File tempFile) {

//        url.toString() return a String in the format: "file:///mnt/sdcard/myPicture.jpg",
//                whereas url.getPath() returns a String in the format: "/mnt/sdcard/myPicture.jpg",


        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String ext = tempFile.getName().substring(tempFile.getName().lastIndexOf(".") + 1);
        String type = mime.getMimeTypeFromExtension(ext);
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                intent.setDataAndType(contentUri, type);
            } else {
                intent.setDataAndType(Uri.fromFile(tempFile), type);
            }
            context.startActivity(intent);
        } catch (ActivityNotFoundException anfe) {
            Toast.makeText(context, "No activity found to open this attachment.", Toast.LENGTH_LONG).show();
        }



//        Intent i = new Intent( Intent.ACTION_VIEW );
//        i.setDataAndType( localUri, MIME_TYPE );
//        context.startActivity( i );
    }


    /**
     * Checks if any apps are installed that supports reading of PDF files.
     * @param context
     * @return
     */
    public static boolean isPDFSupported( Context context,String MIME_TYPE) {
        Intent i = new Intent( Intent.ACTION_VIEW );
        final File tempFile = new File( context.getExternalFilesDir( Environment.DIRECTORY_DOWNLOADS ), "test.pdf" );
        i.setDataAndType( Uri.fromFile( tempFile ), MIME_TYPE );
        return context.getPackageManager().queryIntentActivities( i, PackageManager.MATCH_DEFAULT_ONLY ).size() > 0;
    }
}