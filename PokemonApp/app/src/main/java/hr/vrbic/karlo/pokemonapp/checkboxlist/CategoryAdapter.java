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
import hr.vrbic.karlo.pokemonapp.model.Category;

public class CategoryAdapter extends ArrayAdapter<CategoryAdapter.CategoryItem> {

    private List<CategoryItem> items;
    private CompoundButton.OnCheckedChangeListener listener;
    private LayoutInflater inflater;

    public CategoryAdapter(Context context, List<Category> categories, CompoundButton.OnCheckedChangeListener listener) {
        super(context, R.layout.checkbox_list_row);
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
        items = new ArrayList<>();
        for (Category category : categories) {
            items.add(new CategoryItem(category));
        }
        addAll(items);
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

        CategoryItem item = items.get(position);

        holder.tvOption.setText(item.name);
        holder.checkBox.setChecked(item.isSelected);
        holder.checkBox.setTag(item);
        holder.checkBox.setOnCheckedChangeListener(listener);

        return view;
    }

    public List<Integer> getSelected() {
        List<Integer> selected = new ArrayList<>();
        for (CategoryItem item : items) {
            if (item.isSelected) {
                selected.add(item.id);
            }
        }
        return !selected.isEmpty() ? selected : null;
    }

    public Category getCategory(int position) {
        return items.get(position).getCategory();
    }

    public CategoryItem getCategoryItem(int position) {
        return items.get(position);
    }

    public boolean isChecked() {
        List<Integer> selected = getSelected();
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

    public static class CategoryItem {
        private int id;
        private String name;
        private boolean isSelected;

        public CategoryItem(int id, String name, boolean isSelected) {
            this.id = id;
            this.name = name;
            this.isSelected = isSelected;
        }

        public CategoryItem(int id, String name) {
            this(id, name, false);
        }

        public CategoryItem(hr.vrbic.karlo.pokemonapp.model.Category category) {
            this(category.getId(), category.getName());
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public Category getCategory() {
            return new Category(id, name);
        }
    }
}
