package com.happywannyan.Adapter;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.happywannyan.Activities.MessageDetailsPageActivity;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.SetGetMessageDetailsPojo;
import com.happywannyan.R;
import com.happywannyan.Utils.DownloaderAndShowFile;
import com.happywannyan.Utils.Loger;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by su on 10/4/17.
 */

public class MessageDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    String MsgItemTouch;
    ArrayList<SetGetMessageDetailsPojo> messageDetailsPojoArrayList;
    Context mContext;


    private DownloadManager downloadManager;
    private long refid;
    private Uri Download_Uri;
    ArrayList<Long> list = new ArrayList<>();


    public MessageDetailsAdapter(Context mContext, ArrayList<SetGetMessageDetailsPojo> messageDetailsPojoArrayList) {
        this.mContext = mContext;
        this.messageDetailsPojoArrayList = messageDetailsPojoArrayList;
        downloadManager = (DownloadManager)mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        mContext.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new ViewHolder1(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_msg_item_user, parent, false));
        } else {
            return new ViewHolder2(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_msg_item_sender, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        final SetGetMessageDetailsPojo MsgItem = messageDetailsPojoArrayList.get(i);
        Loger.MSG("MsgItem", "" + i);
        switch (viewHolder.getItemViewType()) {
            case 1:
                ViewHolder1 viewHolder1 = (ViewHolder1) viewHolder;


                if (!MsgItem.getDate().equals("")) {
                    viewHolder1.RL_Date.setVisibility(View.VISIBLE);
                    viewHolder1.TXT_date.setText(MsgItem.getDate());
                } else {
                    viewHolder1.RL_Date.setVisibility(View.GONE);
                }


                String[] words = MsgItem.getPostedon().split("\\s");
                viewHolder1.TXT_User_Time.setText(words[words.length - 2] + " " + words[words.length - 1]);


                if (MsgItem.getMsg_lat().length() > 0 && MsgItem.getMsg_long().length() > 0) {
                    viewHolder1.RL_Map.setVisibility(View.VISIBLE);
                    if (!MsgItem.getUrl_location().equals("")) {
                        Picasso.with(mContext).load(MsgItem.getUrl_location()).into(viewHolder1.IMG_User_MAP);
                    }
//                        Glide.with(mContext).load(MsgItem.getString("url_location")).override(600, 600).diskCacheStrategy(DiskCacheStrategy.ALL).into(IMG_User_MAP);
//                        Glide.with(mContext).load("https://maps.googleapis.com/maps/api/staticmap?center=22.585072500000003,88.49047265625003&zoom=13&size=200x200&markers=%@&key=AIzaSyDAS-0Wh-K3QII2h7DgO8bd-f1dSy4lW3M").override(600, 600).diskCacheStrategy(DiskCacheStrategy.ALL).into(IMG_User_MAP);
                    viewHolder1.RL_Map.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String uri = "";
                            uri = String.format(Locale.ENGLISH, "geo:%f,%f", Float.parseFloat(MsgItem.getMsg_lat()), Float.parseFloat(MsgItem.getMsg_long()));
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            intent.setPackage("com.google.android.apps.maps");
                            mContext.startActivity(intent);
                        }
                    });
                } else {
                    viewHolder1.RL_Map.setVisibility(View.GONE);
                }


                if (!MsgItem.getMessage_info().trim().equals("")) {
                    viewHolder1.RL_Attached.setVisibility(View.GONE);
                    viewHolder1.TXT_User_Message.setVisibility(View.VISIBLE);
                    try {
                        viewHolder1.TXT_User_Message.setText(URLDecoder.decode(MsgItem.getMessage_info(), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    viewHolder1.RL_Attached.setVisibility(View.VISIBLE);
                    viewHolder1.TXT_User_Message.setVisibility(View.GONE);
                    if (MsgItem.getMessage_attachment().trim().endsWith(".doc")) {
                        Glide.with(mContext).load(MsgItem.getMessage_attachment()).override(600, 600).placeholder(R.drawable.ic_doc).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder1.IMG_User_Attach);
                        viewHolder1.img_attached_download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MsgItemTouch=MsgItem.getMessage_attachment();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (((MessageDetailsPageActivity) mContext).checkPermissions()) {
                                        DownloadAndShow();
//                                        ShowContentInBrowser(MsgItemTouch);
                                    }
                                }
                                else {
                                    DownloadAndShow();
                                }
                            }
                        });
                    } else if (MsgItem.getMessage_attachment().trim().endsWith(".pdf")) {
                        Glide.with(mContext).load(MsgItem.getMessage_attachment()).override(600, 600).placeholder(R.drawable.ic_pdf).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder1.IMG_User_Attach);
                        viewHolder1.img_attached_download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MsgItemTouch=MsgItem.getMessage_attachment();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (((MessageDetailsPageActivity) mContext).checkPermissions()) {
                                        DownloadAndShow();
//                                        ShowContentInBrowser(MsgItemTouch);
                                    }
                                }
                                else {
                                    DownloadAndShow();
                                }
                            }
                        });
                    } else if (MsgItem.getMessage_attachment().endsWith(".png") || MsgItem.getMessage_attachment().endsWith(".jpg")) {
                        Glide.with(mContext).load(MsgItem.getMessage_attachment()).override(600, 600).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder1.IMG_User_Attach);
                        viewHolder1.img_attached_download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MsgItemTouch=MsgItem.getMessage_attachment();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (((MessageDetailsPageActivity) mContext).checkPermissions()) {
                                        DownloadAndShow();
//                                        ShowContentInBrowser(MsgItemTouch);
                                    }
                                }
                                else {
                                    DownloadAndShow();
                                }
                            }
                        });
                    }
                }


                break;

            case 2:
                ViewHolder2 viewHolder2 = (ViewHolder2) viewHolder;

                if (!MsgItem.getDate().equals("")) {
                    viewHolder2.RL_Date.setVisibility(View.VISIBLE);
                    viewHolder2.TXT_date.setText(MsgItem.getDate());
                } else {
                    viewHolder2.RL_Date.setVisibility(View.GONE);
                }


                String[] words2 = MsgItem.getPostedon().split("\\s");
                viewHolder2.TXT_sender_name_time.setText(words2[words2.length - 2] + " " + words2[words2.length - 1]);


                if (MsgItem.getMsg_lat().length() > 0 && MsgItem.getMsg_long().length() > 0) {
                    viewHolder2.RL_Map.setVisibility(View.VISIBLE);

                    if (!MsgItem.getUrl_location().equals("")) {
                        Picasso.with(mContext).load(MsgItem.getUrl_location()).into(viewHolder2.IMG_Sender_Map);
                    }

//                        Glide.with(mContext).load(MsgItem.getString("url_location")).override(600, 600).diskCacheStrategy(DiskCacheStrategy.ALL).into(IMG_Sender_Map);
                    viewHolder2.RL_Map.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String uri = "";
                            uri = String.format(Locale.ENGLISH, "geo:%f,%f", Float.parseFloat(MsgItem.getMsg_lat()), Float.parseFloat(MsgItem.getMsg_long()));
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            mContext.startActivity(intent);
                        }
                    });
                } else {
                    viewHolder2.RL_Map.setVisibility(View.GONE);
                }

                if (!MsgItem.getUsersimage().trim().equals("")) {
                    Glide.with(mContext).load(MsgItem.getUsersimage().trim()).override(600, 600).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder2.IMG_SenderUser);
                }

                if (!MsgItem.getMessage_info().equals("")) {
                    viewHolder2.RL_Attached.setVisibility(View.GONE);
                    viewHolder2.TXT_Sender_message.setVisibility(View.VISIBLE);
                    viewHolder2.TXT_Sender_message.setText(MsgItem.getMessage_info());
                } else {
                    viewHolder2.RL_Attached.setVisibility(View.VISIBLE);
                    viewHolder2.TXT_Sender_message.setVisibility(View.GONE);
                    if (MsgItem.getMessage_attachment().trim().endsWith(".doc")) {
                        Glide.with(mContext).load(MsgItem.getMessage_attachment()).override(600, 600).placeholder(R.drawable.ic_doc).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder2.IMG_Sender_Attach);
                        viewHolder2.img_attached_download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MsgItemTouch=MsgItem.getMessage_attachment();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (((MessageDetailsPageActivity) mContext).checkPermissions()) {
                                        DownloadAndShow();
//                                        ShowContentInBrowser(MsgItemTouch);
                                    }
                                }
                                else {
                                    DownloadAndShow();
                                }
                            }
                        });
                    } else if (MsgItem.getMessage_attachment().trim().endsWith(".pdf")) {
                        Glide.with(mContext).load(MsgItem.getMessage_attachment()).override(600, 600).placeholder(R.drawable.ic_pdf).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder2.IMG_Sender_Attach);
                        viewHolder2.img_attached_download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MsgItemTouch=MsgItem.getMessage_attachment();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (((MessageDetailsPageActivity) mContext).checkPermissions()) {
                                        DownloadAndShow();
//                                        ShowContentInBrowser(MsgItemTouch);
                                    }
                                }
                                else {
                                    DownloadAndShow();
                                }
                            }
                        });
                    } else if (MsgItem.getMessage_attachment().trim().endsWith(".png") || MsgItem.getMessage_attachment().trim().endsWith(".jpg")) {
                        Glide.with(mContext).load(MsgItem.getMessage_attachment()).override(600, 600).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder2.IMG_Sender_Attach);
                        viewHolder2.img_attached_download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MsgItemTouch=MsgItem.getMessage_attachment();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (((MessageDetailsPageActivity) mContext).checkPermissions()) {
                                        DownloadAndShow();
//                                        ShowContentInBrowser(MsgItemTouch);
                                    }
                                }
                                else {
                                    DownloadAndShow();
                                }
                            }
                        });
                    }
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messageDetailsPojoArrayList.size();
    }

    private class ViewHolder1 extends RecyclerView.ViewHolder {
        private SFNFTextView TXT_date, TXT_User_Time, TXT_User_Message;
        private ImageView IMG_User_Attach, IMG_User_MAP,img_attached_download;
        RelativeLayout RL_Attached, RL_Map;
        private RelativeLayout RL_Date;

        private ViewHolder1(View itemView) {
            super(itemView);
            RL_Attached = (RelativeLayout) itemView.findViewById(R.id.RL_Attached);
            RL_Map = (RelativeLayout) itemView.findViewById(R.id.RL_Map);
            TXT_date = (SFNFTextView) itemView.findViewById(R.id.TXT_date);
            TXT_User_Time = (SFNFTextView) itemView.findViewById(R.id.TXT_User_Time);
            TXT_User_Message = (SFNFTextView) itemView.findViewById(R.id.TXT_User_Message);
            IMG_User_Attach = (ImageView) itemView.findViewById(R.id.IMG_User_Attach);
            IMG_User_MAP = (ImageView) itemView.findViewById(R.id.IMG_User_MAP);
            RL_Date = (RelativeLayout) itemView.findViewById(R.id.RL_Date);
            img_attached_download= (ImageView) itemView.findViewById(R.id.img_attached_download);
        }
    }

    private class ViewHolder2 extends RecyclerView.ViewHolder {
        private SFNFTextView TXT_date, TXT_Sender_message, TXT_sender_name_time;
        private ImageView IMG_Sender_Attach, IMG_SenderUser, IMG_Sender_Map,img_attached_download;
        private RelativeLayout RL_Attached, RL_Map;
        private RelativeLayout RL_Date;

        private ViewHolder2(View itemView) {
            super(itemView);
            RL_Date = (RelativeLayout) itemView.findViewById(R.id.RL_Date);
            TXT_date = (SFNFTextView) itemView.findViewById(R.id.TXT_date);
            TXT_Sender_message = (SFNFTextView) itemView.findViewById(R.id.TXT_Sender_message);
            TXT_sender_name_time = (SFNFTextView) itemView.findViewById(R.id.TXT_sender_name_time);
            IMG_Sender_Attach = (ImageView) itemView.findViewById(R.id.IMG_Sender_Attach);
            IMG_SenderUser = (ImageView) itemView.findViewById(R.id.IMG_SenderUser);
            IMG_Sender_Map = (ImageView) itemView.findViewById(R.id.IMG_Sender_Map);
            RL_Attached = (RelativeLayout) itemView.findViewById(R.id.RL_Attached);
            RL_Map = (RelativeLayout) itemView.findViewById(R.id.RL_Map);
            img_attached_download= (ImageView) itemView.findViewById(R.id.img_attached_download);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        if (messageDetailsPojoArrayList.get(position).getSender_id().equals(AppConstant.UserId)) {
            return 1;
        } else {
            return 2;
        }
    }


    public void DownloadAndShow(){
        DownloaderAndShowFile.downloadAndOpenPDF(mContext,MsgItemTouch);

//        final String filename = MsgItemTouch.substring( MsgItemTouch.lastIndexOf( "/" ) + 1 );
//
//        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(MsgItemTouch));
//        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
//        request.setAllowedOverRoaming(false);
//        request.setTitle("Attached File Downloading " + filename);
//        request.setDescription("Downloading " + filename);
//        request.setVisibleInDownloadsUi(true);
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/HappyWanNyan/"  + "/" + filename);
//
//
//        refid = downloadManager.enqueue(request);
//
//        Loger.MSG("OUT", "" + refid);
//
//        list.add(refid);
    }


    BroadcastReceiver onComplete = new BroadcastReceiver() {

        public void onReceive(Context ctxt, Intent intent) {

            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            Loger.MSG("IN", "" + referenceId);

            list.remove(referenceId);

            if (list.isEmpty())
            {

                Loger.MSG("INSIDE", "" + referenceId);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(mContext)
                                .setSmallIcon(R.mipmap.ic_launcher1)
                                .setContentTitle(mContext.getResources().getString(R.string.app_name))
                                .setContentText(mContext.getResources().getString(R.string.download_complete));

                NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(455, mBuilder.build());
            }
        }
    };


    public void unRegister(){
        mContext.unregisterReceiver(onComplete);
    }



//    public void ShowContentInBrowser(String url) {
//        Loger.MSG("url-->",""+url);
//        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//        mContext.startActivity(browserIntent);
//    }
}