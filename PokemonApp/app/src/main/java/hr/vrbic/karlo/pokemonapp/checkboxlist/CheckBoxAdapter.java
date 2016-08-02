package hr.vrbic.karlo.pokemonapp.checkboxlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hr.vrbic.karlo.pokemonapp.R;

public class CheckBoxAdapter<T extends CheckBoxItem> extends ArrayAdapter<CheckBoxItem> {

    private ArrayList<T> items;
    private CompoundButton.OnCheckedChangeListener listener;
    private LayoutInflater inflater;

    public CheckBoxAdapter(Context context, List<T> items, CompoundButton.OnCheckedChangeListener listener) {
        super(context, R.layout.checkbox_list_row);
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
        this.items = new ArrayList<>(items);
        addAll(this.items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        CategoryHolder holder = null;

        if (convertView == null) {
            view = inflater.inflate(R.layout.checkbox_list_row, parent, false);
            holder = new CategoryHolder(view);
            view.setTag(holder);
        } else {
            holder = (CategoryHolder) view.getTag();
        }

        CheckBoxItem item = items.get(position);

        holder.tvOption.setText(item.getName());
        holder.checkBox.setChecked(item.isSelected());
        holder.checkBox.setTag(item);
        holder.checkBox.setOnCheckedChangeListener(listener);

        return view;
    }

    public ArrayList<T> getAll() {
        return items;
    }

    public List<T> getSelected() {
        List<T> selected = new ArrayList<>();
        for (T item : items) {
            if (item.isSelected()) {
                selected.add(item);
                item.setSelected(false);
            }
        }
        return !selected.isEmpty() ? selected : null;
    }

    public T getCheckBoxItem(int position) {
        return items.get(position);
    }


    public boolean isChecked() {
        List<T> selected = getSelected();
        if(selected == null){
            return false;
        } else {
            return !getSelected().isEmpty();
        }
    }

    class CategoryHolder {

        @BindView(R.id.chk_box)
        CheckBox checkBox;
        @BindView(R.id.tv_option)
        TextView tvOption;

        public CategoryHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
