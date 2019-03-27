package net.lzzy.cinemanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.fragments.AddCinemasFragment;
import net.lzzy.cinemanager.fragments.AddOrdersFragment;
import net.lzzy.cinemanager.fragments.CinemasFragment;
import net.lzzy.cinemanager.fragments.OrdersFragment;

import static net.lzzy.cinemanager.R.id.*;

/**
 * @author Administrator
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
private FragmentManager managar=getSupportFragmentManager();
    private LinearLayout layoutMenu;
    private TextView tvTitle;
    private SearchView search;
    private SparseArray<String> titleArray=new SparseArray<>();
    private SparseArray<Fragment> fragmentArray = new SparseArray<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setTitleMenu();
    }

    private void setTitleMenu() {
        titleArray.put(bar_title_tv_add_cinema,"添加影院");
        titleArray.put(bar_title_tv_add_cinema,"影院列表");
        titleArray.put(bar_title_tv_add_order,"添加订单");
        titleArray.put(bar_title_tv_add_order,"我的订单");
        layoutMenu = findViewById(bar_title_layout_menu);
        layoutMenu.setVisibility(View.GONE);
        findViewById(bar_title_img_menu).setOnClickListener(v -> {
            int visible=layoutMenu.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE;
            layoutMenu.setVisibility(visible);
        });
        tvTitle = findViewById(bar_title_tv_title);
        tvTitle.setText(R.string.bar_title_menu_orders);
        search = findViewById(main_sv_search);
        findViewById(bar_title_tv_view_cinema).setOnClickListener(this);
        findViewById(bar_title_tv_add_order).setOnClickListener(this);
        findViewById(bar_title_tv_add_cinema).setOnClickListener(this);
        findViewById(bar_title_tv_view_order).setOnClickListener(this);
        findViewById(bar_title_tv_exit).setOnClickListener(this);
        findViewById(bar_title_tv_exit).setOnClickListener(v -> System.exit(0));
    }

    @Override
    public void onClick(View v) {
        layoutMenu.setVisibility(View.GONE);
        tvTitle.setText(titleArray.get(v.getId()));
        FragmentTransaction transaction = managar.beginTransaction();
        Fragment fragment = fragmentArray.get(v.getId());
        if (fragment == null) {
            fragment = createFragment(v.getId());
            fragmentArray.put(v.getId(), fragment);
            transaction.add(fragment_container, fragment);
        }
        for (Fragment f : managar.getFragments()) {
            transaction.hide(f);
        }
        transaction.show(fragment).commit();
    }
        private Fragment createFragment(int id) {
        switch (id){
                case R.id.bar_title_tv_add_cinema:
                    return new AddCinemasFragment();

                case R.id.bar_title_tv_view_cinema:
                    return new CinemasFragment();

                case R.id.bar_title_tv_add_order:
                   return new AddOrdersFragment();

                case R.id.bar_title_tv_view_order:
                    return new OrdersFragment();

                    default:
                        break;
            }
            return null;

        }
    }

