package com.prembros.programming.quizapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.Toast;

import com.prembros.programming.quizapp.Bookmarks.PlaceholderFragment;

import java.util.List;

/*
 * Created by Prem $ on 8/8/2016.
 */
public class BookmarksAdapter extends BaseAdapter {

    private Context _context;
    private List<String> bookmarkHeader;
    private List<String> bookmarkChild;
    private boolean doubleBackToDelete = false;

    public BookmarksAdapter(Context context, List<String> header, List<String> child) {
        this._context = context;
        bookmarkHeader = header;
        bookmarkChild = child;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public int getCount() {
        return bookmarkHeader.size() & bookmarkChild.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    public View getView(final int position, View convertView, ViewGroup parent) {
        CustomTextViewSemiLight bookmarked_question;
        CustomTextViewLight bookmarked_answer;

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.bookmark_list_item, null);
        }

        bookmarked_question = (CustomTextViewSemiLight) convertView.findViewById(R.id.bookmark_header_textView);
        if (bookmarked_question != null) {
            bookmarked_question.setText(bookmarkHeader.get(position));
        }

        bookmarked_answer = (CustomTextViewLight) convertView.findViewById(R.id.bookmark_child_textView);
        if (bookmarked_answer != null) {
            bookmarked_answer.setText(bookmarkChild.get(position));
        }

        ImageButton deleteBookmarkedItem = (ImageButton) convertView.findViewById(R.id.delete_bookmarked_item);
        deleteBookmarkedItem.setFocusableInTouchMode(false);
        deleteBookmarkedItem.setFocusable(false);
        deleteBookmarkedItem.setClickable(true);

        deleteBookmarkedItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final View view = (View) v.getParent();
                if (doubleBackToDelete) {
                    view.startAnimation(AnimationUtils.loadAnimation(_context, R.anim.fragment_anim_out));
                    PlaceholderFragment.listView.startAnimation(
                            AnimationUtils.loadAnimation(_context, android.R.anim.fade_out));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            DatabaseHolder db = new DatabaseHolder(_context);
                            db.open();
                            if (position >= 0
                                    && position <= bookmarkHeader.size()
                                    && !db.returnQuestion().isAfterLast()) {
                                    db.deleteData(bookmarkHeader.get(position));
                                bookmarkHeader.remove(position);
                                bookmarkChild.remove(position);
                            }
                            db.close();
//                            PlaceholderFragment.listView.setVisibility(View.INVISIBLE);
                            notifyDataSetChanged();
                            if (bookmarkHeader.isEmpty()){
                            Bookmarks.PlaceholderFragment.listItemInvalidate();
                            }
                            PlaceholderFragment.listView.startAnimation(
                                    AnimationUtils.loadAnimation(_context, android.R.anim.fade_in));
//                            PlaceholderFragment.listView.setVisibility(View.VISIBLE);
                        }
                    }, 300);
                    doubleBackToDelete = false;
                    return;
                }

                doubleBackToDelete = true;
                Toast.makeText(_context, "Hit again to delete bookmark.", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToDelete = false;
                    }
                }, 2000);
            }
        });

        deleteBookmarkedItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(_context, "Delete this bookmark", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        return convertView;
    }
}