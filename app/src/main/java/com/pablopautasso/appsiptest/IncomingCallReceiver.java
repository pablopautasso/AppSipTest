package com.pablopautasso.appsiptest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.sip.SipAudioCall;
import android.net.sip.SipProfile;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class IncomingCallReceiver extends BroadcastReceiver{


    public final String TAG = "INCOMINGCALL";
    /**
     * Processes the incoming call, answers it, and hands it over to the
     * WalkieTalkieActivity.
     * @param context The context under which the receiver is running.
     * @param intent The intent being received.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        SipAudioCall incomingCall = null;
        Toast.makeText(context, "HAY UNA LLAMADA ",Toast.LENGTH_LONG).show();
        final  Ringtone ringtone;
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(context, uri);
        try {

            SipAudioCall.Listener listener = new SipAudioCall.Listener() {

                @Override
                public void onRinging(SipAudioCall call, SipProfile caller) {
                    Log.d(TAG, "onRinging: e.");
                    try {
                        call.answerCall(30);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

//                @Override
////                public void  (SipAudioCall call){
////                    Log.d(TAG, "onRingingBack: ..");
////                    ringtone.play();
////                }


                @Override
                public void onCalling(SipAudioCall call) {
                    super.onCalling(call);
                    Log.d(TAG, "onRingingBack: ..");
                    ringtone.play();
                }

                @Override
                public void onCallEstablished(SipAudioCall call) {
                    Log.d(TAG, "onCallEstablished: ..");
                    ringtone.stop();
                    call.startAudio();
                }
            };

            MainActivity wtActivity = (MainActivity) context;

            incomingCall = wtActivity.manager.takeAudioCall(intent, listener);
            incomingCall.answerCall(30);
            incomingCall.startAudio();
            incomingCall.setSpeakerMode(true);
            if(incomingCall.isMuted()) {
                incomingCall.toggleMute();
            }

            wtActivity.call = incomingCall;

            wtActivity.updateStatus(incomingCall);

        } catch (Exception e) {
            if (incomingCall != null) {
                incomingCall.close();
            }
        }
    }
}
