package com.happywannyan.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.happywannyan.Adapter.Login_FragmentviewPager;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.Login_Slider.slider1;
import com.happywannyan.Login_Slider.slider2;
import com.happywannyan.Login_Slider.slider3;
import com.happywannyan.Login_Slider.slider4;
import com.happywannyan.R;

import java.util.LinkedList;

public class LoginChoser extends AppCompatActivity  implements View.OnClickListener{
    LinkedList<Fragment> fragmentList;
    ViewPager viewPager;
    private int dotscount=0;
    private ImageView[] dots;
    LinearLayout LL_dotc;
    LinearLayout LL_SINGIN;
    LinearLayout LL_Facbook;
    LinearLayout LL_SIGNUP;

    SFNFTextView Txt_skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_choser);
        viewPager=(ViewPager)findViewById(R.id.viewpager) ;
        LL_dotc=(LinearLayout)findViewById(R.id.LL_dotc);
        LL_SINGIN=(LinearLayout)findViewById(R.id.LL_SINGIN);
        LL_SINGIN.setOnClickListener(this);
        LL_Facbook=(LinearLayout)findViewById(R.id.LL_Facbook);
        LL_Facbook.setOnClickListener(this);
        LL_SIGNUP=(LinearLayout)findViewById(R.id.LL_SIGNUP);
        LL_SIGNUP.setOnClickListener(this);
        Txt_skip=(SFNFTextView)findViewById(R.id.TXT_skip);
        Txt_skip.setOnClickListener(this);
        fragmentList = new LinkedList<>();


        fragmentList.add(new slider1());
        fragmentList.add(new slider2());
        fragmentList.add(new slider3());
        fragmentList.add(new slider4());

        Login_FragmentviewPager adapter=new Login_FragmentviewPager(this,getSupportFragmentManager(),fragmentList);

        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        UIindicator(fragmentList.size());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i=0;i<fragmentList.size();i++)
                {
                    dots[i].setImageResource(R.drawable.ic_dot_outline);
                }
                dots[position].setImageResource(R.drawable.ic_dot);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }

    private void UIindicator(int totalcount){
        dots=new ImageView[totalcount];
        for(int i=0;i<totalcount;i++){
            dots[i]=new ImageView(this);
            dots[i].setImageResource(R.drawable.ic_dot_outline);
            dots[i].setPadding(10,0,10,0);
            LL_dotc.addView(dots[i]);
        }
        dots[0].setImageResource(R.drawable.ic_dot);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.TXT_skip:
                startActivity(new Intent(LoginChoser.this,BaseActivity.class));
                finish();
                break;
            case R.id.LL_Facbook:
                startActivityForResult(new Intent(LoginChoser.this,FacebookActivity.class),FacebookActivity.FacebookResponse);
                break;
            case R.id.LL_SINGIN:
                startActivity(new Intent(LoginChoser.this,LoginActivity.class));
                    finish();
                break;

            case R.id.LL_SIGNUP:
                startActivity(new Intent(LoginChoser.this,SignUpActivity.class));
//                finish();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==FacebookActivity.FacebookResponse && resultCode==RESULT_OK)
        {
            startActivity(new Intent(LoginChoser.this,BaseActivity.class));
            finish();
        }

    }
}
