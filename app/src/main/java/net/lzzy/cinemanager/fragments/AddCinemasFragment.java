package net.lzzy.cinemanager.fragments;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.style.cityjd.JDCityPicker;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Cinema;

/**
 * Created by lzzy_gxy on 2019/3/27.
 * Description:
 */
public class AddCinemasFragment extends BaseFragment {
    private String province="广西壮族自治区";
    private String city="柳州";
    private String area="鱼峰区";
    private OnFragmentInteractionListener Listener;
    private OnCinemaCreatedListener cinemaListener;
    private TextView tvArea;
    private EditText edtName;

    @Override
    protected void populate() {
        tvArea = find(R.id.dialog_add_tv_area);
        edtName = find(R.id.dialog_add_cinema_edt_name);
        Listener.hideSearch();
        find(R.id.dialog_add_cinema_btn_cancel).setOnClickListener(v -> {
            cinemaListener.cancelAddCinema();
        });
        find(R.id.dialog_add_cinema_layout_area).setOnClickListener(v ->{
            showLocation();
        });

        find(R.id.dialog_add_cinema_btn_save).setOnClickListener(v -> {
            String name= edtName.getText().toString();
            String location=tvArea.getText().toString();
            if(name.isEmpty()){
                Toast.makeText(getActivity(),"影院不能为空",Toast.LENGTH_SHORT).show();
            }else {
                Cinema cinema=new Cinema();
                cinema.setName(name);
                cinema.setArea(area);
                cinema.setCity(city);
                cinema.setProvince(province);
                cinema.setLocation(location);
                edtName.setText("");
                cinemaListener.saveCinema(cinema);
            }

        });

    }

    private void showLocation() {
        JDCityPicker cityPicker=new JDCityPicker();
        cityPicker.init(getContext());
        cityPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                super.onSelected(province, city, district);
                AddCinemasFragment.this.province=province.getName();
                AddCinemasFragment.this.city=city.getName();
                AddCinemasFragment.this.area=district.getName();
                String loc=province.getName()+city.getName()+district.getName();
                tvArea.setText(loc);
            }
            @Override
            public void onCancel() {
            }

        });
        cityPicker.showCityPicker();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.add_fragment_cinemas;
    }

    @Override
    public void search(String kw) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            Listener=(OnFragmentInteractionListener) context;
           cinemaListener=(OnCinemaCreatedListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    +"必须实现接口+OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Listener=null;
        cinemaListener=null;
    }
    public interface OnCinemaCreatedListener{
        void cancelAddCinema();

        void saveCinema(Cinema cinema);
    }


}