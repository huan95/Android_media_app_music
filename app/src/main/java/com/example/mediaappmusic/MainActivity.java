package com.example.mediaappmusic;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    TextView txtTile, txtTimeSong, txtTimeTotal;
    SeekBar skSong;
    ImageButton btnPrev, btnPlay, btnNext;
    ImageView imgDisc;

    ArrayList<Song> arraySong;
    int position = 0;
    MediaPlayer mediaPlayer;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handling();
        AddSong();
        CreateMediaPlayer();
        animation = AnimationUtils.loadAnimation(this, R.anim.disc_rolate);

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position--;
                if (position < 0){
                    position = arraySong.size() - 1;
                }
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                CreateMediaPlayer();
                mediaPlayer.start();
                btnPlay.setImageResource(R.drawable.ic_stop);
                SetTimeTotal();
                UpdateTimeSong();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position++;
                if (position > arraySong.size() - 1){
                    position = 0;
                }
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                CreateMediaPlayer();
                mediaPlayer.start();
                btnPlay.setImageResource(R.drawable.ic_stop);
                SetTimeTotal();
                UpdateTimeSong();
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()){
                    //neu dang phat -> pauser -> doi hinh play
                    mediaPlayer.pause();
                    btnPlay.setImageResource(R.drawable.ic_play);
                    imgDisc.clearAnimation();

                } else {
                    //dang ngung -> phat -> doi hinh pause
                    mediaPlayer.start();
                    btnPlay.setImageResource(R.drawable.ic_stop);
                    imgDisc.startAnimation(animation);
                }
                SetTimeTotal();
                UpdateTimeSong();
            }
        });

        skSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(skSong.getProgress());
            }
        });
    }

    private void Handling(){
        txtTimeSong     = (TextView) findViewById(R.id.textViewTimeSong);
        txtTimeTotal    = (TextView) findViewById(R.id.textViewTimeTotal);
        txtTile         = (TextView) findViewById(R.id.textViewNameSingle);
        skSong          = (SeekBar) findViewById(R.id.seekBar);
        btnPrev         = (ImageButton) findViewById(R.id.imageButtonBack);
        btnPlay         = (ImageButton) findViewById(R.id.imageButtonStart);
        btnNext         = (ImageButton) findViewById(R.id.imageButtonNext);
        imgDisc         = (ImageView) findViewById(R.id.imageViewDisc);
    }

    private void AddSong(){
        arraySong = new ArrayList<>();
        arraySong.add(new Song("Đời là thế thôi", R.raw.doi_la_the_thoi_cham_mat_giang_ho_phu_le));
        arraySong.add(new Song("Đúng người đúng thời điểm", R.raw.dung_nguoi_dung_thoi_diem_thanh_hung));
        arraySong.add(new Song("Em sẽ là cô ", R.raw.em_se_la_co_dau_minh_vuong_huy_cung));
        arraySong.add(new Song("Anh chẳng sao ", R.raw.anh_chang_sao_ma_khang_viet));
    }

    private void CreateMediaPlayer(){
        mediaPlayer = MediaPlayer.create(MainActivity.this, arraySong.get(position).getFile());
        txtTile.setText(arraySong.get(position).getTitle());
    }

    private void SetTimeTotal(){
        SimpleDateFormat hourFormat = new SimpleDateFormat("mm:ss");
        txtTimeTotal.setText(hourFormat.format(mediaPlayer.getDuration()));
        //gan max cuar skSong = mediaPlayer.getDuration()
        skSong.setMax(mediaPlayer.getDuration());
    }

    private void UpdateTimeSong(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat hourFormat = new SimpleDateFormat("mm:ss");
                txtTimeSong.setText(hourFormat.format(mediaPlayer.getCurrentPosition()));
                //update progess skSong
                skSong.setProgress(mediaPlayer.getCurrentPosition());
                //kiem tra thoi gian bai hat neu ket thuc -> next
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        position++;
                        if (position > arraySong.size() - 1){
                            position = 0;
                        }
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                        }
                        CreateMediaPlayer();
                        mediaPlayer.start();
                        btnPlay.setImageResource(R.drawable.ic_stop);
                        SetTimeTotal();
                        UpdateTimeSong();
                    }
                });
                handler.postDelayed(this, 500);
            }
        }, 100);
    }
}
