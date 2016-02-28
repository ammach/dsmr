package com.example.ammach.dsmr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.SendButton;
import com.facebook.share.widget.ShareButton;

public class ActivitySharing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_sharing);

        ShareButton shareButton = (ShareButton)findViewById(R.id.fb_share_button);
        SendButton sendButton = (SendButton)findViewById(R.id.fb_share_image);


        ShareLinkContent contentLink = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://www.facebook.com/groups/IRISI2017/"))
                .setImageUrl(Uri.parse("http://m.c.lnkd.licdn.com/media/p/1/000/22a/182/184c258.png"))
                .setContentDescription("Description")
                .build();


        Bitmap image = BitmapFactory.decodeResource(getResources(),R.drawable.amine);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .setCaption("Give me my codez or I will ... you know, do that thing you don't like!")
                .build();
        SharePhotoContent contentImage = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        shareButton.setShareContent(contentLink);
        sendButton.setShareContent(contentImage);

    }
}
