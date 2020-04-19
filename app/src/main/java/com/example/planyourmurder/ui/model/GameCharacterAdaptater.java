package com.example.planyourmurder.ui.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.planyourmurder.R;

import org.w3c.dom.Text;

import java.util.LinkedList;

public class GameCharacterAdaptater extends ArrayAdapter<GameCharacter> {
    private final Context _context;
    private LinkedList<GameCharacter> _chars;

    public GameCharacterAdaptater(Context context, int resource, LinkedList<GameCharacter> chars){
        super(context,resource, chars);
        _context = context;
        _chars = chars;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_item, parent, false);
        } else {
            convertView = (LinearLayout) convertView;
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imchar);
        imageView.setImageBitmap(_chars.get(position).getImage());

        TextView viewName = (TextView) convertView.findViewById(R.id.namechar);
        viewName.setText(_chars.get(position).getName());
        viewName.setTag(_chars.get(position).getName());

        return convertView;
    }

}
