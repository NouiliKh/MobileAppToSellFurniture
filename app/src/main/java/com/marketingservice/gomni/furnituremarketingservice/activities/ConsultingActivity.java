package com.marketingservice.gomni.furnituremarketingservice.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.marketingservice.gomni.furnituremarketingservice.R;
import com.marketingservice.gomni.furnituremarketingservice.modal.Product;
import com.marketingservice.gomni.furnituremarketingservice.sql.SqliteHelper;

import java.util.ArrayList;
import java.util.List;




    public class ConsultingActivity extends AppCompatActivity {



        private static RecyclerView.Adapter adapter;
        private RecyclerView.LayoutManager layoutManager;
        RecyclerView recyclerView;

        static View.OnClickListener myOnClickListener;
        private static ArrayList<Integer> removedItems;

        FloatingActionButton buttonAdd;
        SqliteHelper sqlHelper;

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            sqlHelper = new SqliteHelper(this);

            super.onCreate(savedInstanceState);
            setContentView(R.layout.content_consulting);
            initViews();

            myOnClickListener = new MyOnClickListener(this);

            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);

            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            final List<Product> data = sqlHelper.listProducts();

            removedItems = new ArrayList<Integer>();

            adapter = new CustomAdapter(data);
            recyclerView.setAdapter(adapter);


            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ConsultingActivity.this, AddProductActivity.class);
                    startActivity(intent);
                }
            });

            SwipeableRecyclerViewTouchListener swipeTouchListener =
                    new SwipeableRecyclerViewTouchListener(recyclerView,
                            new SwipeableRecyclerViewTouchListener.SwipeListener() {
                                @Override
                                public boolean canSwipe(int position) {

                                    return true;
                                }

                                @Override
                                public boolean canSwipeLeft(int position) {
                                    return true;
                                }

                                @Override
                                public boolean canSwipeRight(int position) {
                                    return true;

                                }

                                @Override
                                public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                    for (int position : reverseSortedPositions) {
                                        sqlHelper.deleteProduct(data.get(position).id);
                                        data.remove(position);
                                        adapter.notifyItemRemoved(position);

                                    }
                                    adapter.notifyDataSetChanged();
                                }


                                @Override
                                public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                    for (int position : reverseSortedPositions) {

                                        Intent intent = new Intent(ConsultingActivity.this, EditActivity.class);
                                        intent.putExtra("id", data.get(position).id);
                                        startActivity(intent);


                                    }
                                    adapter.notifyDataSetChanged();

                                }
                            });

           recyclerView.addOnItemTouchListener(swipeTouchListener);

        }


        private static class MyOnClickListener implements View.OnClickListener {

            private final Context context;

            private MyOnClickListener(Context context) {
                this.context = context;
            }

            @Override
            public void onClick(View v) {

            }


        }



        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            super.onOptionsItemSelected(item);

            return true;
        }



        private void initViews() {

          buttonAdd = (FloatingActionButton) findViewById(R.id.fab);
        }

    }


