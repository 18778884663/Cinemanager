package net.lzzy.cinemanager.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.session.PlaybackState;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.Touch;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.models.CinemaFactory;
import net.lzzy.cinemanager.models.Order;
import net.lzzy.cinemanager.models.OrderFactory;
import net.lzzy.cinemanager.utils.AppUtils;
import net.lzzy.simpledatepicker.CustomDatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by lzzy_gxy on 2019/3/27.
 * Description:
 */
public class AddOrdersFragment extends BaseFragment{

    private OnFragmentInteractionListener Listener;
    private OnOrderCreatedListener orderlistener;
    private EditText edtMovieName;
    private TextView tvTime;
    private Spinner spCinemas;
    private EditText edtMoviePrice;
    private CustomDatePicker datePicker;
    private ArrayAdapter<Cinema> adapter;
    private List<Cinema> cinemas;
    private OrderFactory factory=OrderFactory.getInstance();
    private List<Order> orders;
    private ImageView imgQRCode;




    @Override
    protected void populate() {
        Listener.hideSearch();
        edtMovieName=find(R.id.add_order_movie_edt_name);
        tvTime=find(R.id.add_order_tv_time);
        spCinemas=find(R.id.add_order_sp_cinemas);
        edtMoviePrice = find(R.id.add_order_movie_edt_price);
        imgQRCode = find(R.id.add_order_movie_imgQRCode);

        orders = factory.get();
        cinemas= CinemaFactory.getInstance().get();
        spCinemas.setAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, cinemas));

        initDatePicker();
        find(R.id.add_order_layout_time).setOnClickListener(v -> datePicker.show(tvTime.getText().toString()));
        find(R.id.add_order_movie_btn_QR_code).setOnClickListener(v -> {
            String name=edtMovieName.getText().toString();
            String price=edtMoviePrice.getText().toString();
            String location=spCinemas.getSelectedItem().toString();
            String time=tvTime.getText().toString();
            if(TextUtils.isEmpty(name)||TextUtils.isEmpty(price)) {
                Toast.makeText(getActivity(), "信息需要完整", Toast.LENGTH_SHORT).show();
                return;
            }
            String content="["+name+"]"+time+"\n"+location+"  票价为："+price+"元";
            imgQRCode.setImageBitmap(AppUtils.createQRCodeBitmap(content,200,200));
        });
        find(R.id.add_order_movie_btn_save).setOnClickListener(v -> {
            String name=edtMovieName.getText().toString();
            String moviePrice=edtMoviePrice.getText().toString();
            if (TextUtils.isEmpty(name)||TextUtils.isEmpty(moviePrice)){
                Toast.makeText(getActivity(),"信息需要完整",Toast.LENGTH_SHORT).show();
                return;
            }
            float price;
            try{
                price=Float.parseFloat(moviePrice);
            }catch (NumberFormatException e){
                Toast.makeText(getActivity(),"数字格式错误",Toast.LENGTH_SHORT).show();
                return;
            }
            Order order=new Order();
            Cinema cinema=cinemas.get(spCinemas.getSelectedItemPosition());
            order.setCinemaId(cinema.getId());
            order.setMovie(name);
            order.setMovieTime(tvTime.getText().toString());
            order.setPrice(price);
            orderlistener.saveOrder(order);
            edtMovieName.setText("");
            edtMoviePrice.setText("");

        });
        find(R.id.add_order_movie_btn_cancel).setOnClickListener(v -> {
            orderlistener.cancelAddOrder();
        });

        imgQRCode.setOnLongClickListener(v -> {
            Bitmap bitmap=((BitmapDrawable)imgQRCode.getDrawable()).getBitmap();
            Toast.makeText(getActivity(),AppUtils.readQRCode(bitmap),Toast.LENGTH_SHORT).show();
            return true;
        });


    }



    private void initDatePicker() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
            String now = sdf.format(new Date());
            tvTime.setText(now);
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.MARCH,1);
            String end=sdf.format(calendar.getTime());
            datePicker = new CustomDatePicker(getActivity(), s -> tvTime.setText(s), now, end);
            datePicker.setIsLoop(true);
            datePicker.showSpecificTime(true);
        }




    @Override
    public int getLayoutRes() {
        return R.layout.add_fragment_orders;
    }

    @Override
    public void search(String kw) {

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            Listener.hideSearch();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            Listener=(OnFragmentInteractionListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"必须实现接口+OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Listener=null;
    }
    public interface OnOrderCreatedListener {

        void cancelAddOrder();
        void saveOrder(Order order);
    }

}
