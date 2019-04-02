package net.lzzy.cinemanager.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.text.method.Touch;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.models.CinemaFactory;
import net.lzzy.cinemanager.models.Order;
import net.lzzy.cinemanager.models.OrderFactory;
import net.lzzy.cinemanager.utils.AppUtils;
import net.lzzy.cinemanager.utils.ViewUtils;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;

import java.util.List;

/**
 * Created by lzzy_gxy on 2019/3/26.
 * Description:
 */
public class OrdersFragment extends BaseFragment {
    private static final int MIN_DISTANCE=100;
    private ListView lv;
    private List<Order> orders;
    private GenericAdapter<Order> adapter;
    private OrderFactory factory=OrderFactory.getInstance();
    private Order order;
    private float touchX1;
    private boolean isDelete=false;

    public OrdersFragment(){
    }
    public OrdersFragment(Order order){
        this.order=order;
    }



    @Override
    protected void populate() {
        ListView lv=find(R.id.activity_cinema_lv);
        View empty=find(R.id.activity_cinemas_tv_none);
        lv.setEmptyView(empty);
        orders=factory.get();
        adapter=new GenericAdapter<Order>(getContext(),
                R.layout.cinema_item,orders) {
            @Override
            public void populate(ViewHolder holder, Order order) {
                String location = CinemaFactory.getInstance()
                        .getById(order.getCinemaId().toString()).toString();

                holder.setTextView(R.id.cinemas_items_tv_name, order.getMovie())
                        .setTextView(R.id.cinemas_items_tv_location, location);

                Button btn = holder.getView(R.id.media_actions);
                btn.setOnClickListener(v -> new AlertDialog.Builder(getContext())
                        .setTitle("删除确认")
                        .setMessage("要删除订单吗？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                isDelete = false;
                                adapter.remove(order);
                            }
                        }).show());

                int visible = isDelete ? View.VISIBLE : View.GONE;
                btn.setVisibility(visible);

                holder.getConvertView().setOnTouchListener(new ViewUtils.AbstractTouchHandler() {
                    @Override
                    public boolean handleTouch(MotionEvent event) {
                        slideToDelete(event, order, btn);
                        return true;
                    }
                });
            }


            @Override
            public boolean persistInsert(Order order) {
                return false;
            }

            @Override
            public boolean persistDelete(Order order) {
                return false;
            }
        };
          lv.setAdapter(adapter);
        if (order!=null){
                save(order);
        }

    }
        public void save(Order order){
            adapter.add(order);
        }

        private void slideToDelete(MotionEvent event, Order order, Button btn) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    touchX1=event.getX();
                    break;
                case MotionEvent.ACTION_UP:
                    float touchX2=event.getX();
                    if (touchX1-touchX2 > MIN_DISTANCE){
                        if (!isDelete) {
                            btn.setVisibility(View.VISIBLE);
                            isDelete=true;
                        }
                    }else {
                        if(btn.isShown()){
                            btn.setVisibility(View.GONE);
                            isDelete=false;
                        }else {
                            clickOrder(order);
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        private void clickOrder(Order order) {
            Cinema cinema=CinemaFactory.getInstance().getById(order.getCinemaId().toString());
            String content="["+order.getMovie()+"]"+order.getMovieTime()
                    +"\n"+cinema.toString()+"  票价为："+order.getPrice()+"元";
            View view= LayoutInflater.from(getActivity()).inflate(R.layout.diglog_qrcode,null);
            ImageView img=view.findViewById(R.id.dialog_qrcode_img);
            img.setImageBitmap(AppUtils.createQRCodeBitmap(content,300,300));
            new AlertDialog.Builder(getActivity())
                    .setView(view).show();
        }

        @Override
        public int getLayoutRes() {
            return R.layout.fragment_orders;
        }

        @Override
        public void search(String kw) {
            orders.clear();
            if (TextUtils.isEmpty(kw)){
                orders.addAll(factory.get());
            }else {
                orders.addAll(factory.searchOrders(kw));
            }
            adapter.notifyDataSetChanged();
        }

    }
