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
import hr.vrbic.karlo.pokemonapp.model.Ability;

public class AbilityAdapter extends ArrayAdapter<AbilityAdapter.AbilityItem> {

    private List<AbilityItem> items;
    private CompoundButton.OnCheckedChangeListener listener;
    private LayoutInflater inflater;

    public AbilityAdapter(Context context, List<Ability> abilities, CompoundButton.OnCheckedChangeListener listener) {
        super(context, R.layout.checkbox_list_row);
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
        items = new ArrayList<>();
        for (Ability ability : abilities) {
            items.add(new AbilityItem(ability));
        }
        addAll(items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        AbilityHolder holder = null;

        if (convertView == null) {
            view = inflater.inflate(R.layout.checkbox_list_row, parent, false);
            holder = new AbilityHolder(view);
            view.setTag(holder);
        } else {
            holder = (AbilityHolder) view.getTag();
        }

        AbilityItem item = items.get(position);

        holder.tvOption.setText(item.name);
        holder.checkBox.setChecked(item.isSelected);
        holder.checkBox.setTag(item);
        holder.checkBox.setOnCheckedChangeListener(listener);

        return view;
    }

    public List<Integer> getSelected() {
        List<Integer> selected = new ArrayList<>();
        for (AbilityItem item : items) {
            if (item.isSelected) {
                selected.add(item.id);
            }
        }
        return !selected.isEmpty() ? selected : null;
    }

    public boolean isChecked() {
        List<Integer> selected = getSelected();
        if(selected == null){
            return false;
        } else {
            return !getSelected().isEmpty();
        }
    }

    public Ability getAbility(int position) {
        return items.get(position).getAbility();
    }

    public AbilityItem getAbilityItem(int position) {
        return items.get(position);
    }

    class AbilityHolder {

        @BindView(R.id.chk_box)
        CheckBox checkBox;
        @BindView(R.id.tv_option)
        TextView tvOption;

        public AbilityHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public static class AbilityItem {
        private int id;
        private String name;
        private boolean isSelected;

        public AbilityItem(int id, String name, boolean isSelected) {
            this.id = id;
            this.name = name;
            this.isSelected = isSelected;
        }

        public AbilityItem(int id, String name) {
            this(id, name, false);
        }

        public AbilityItem(Ability ability) {
            this(ability.getId(), ability.getName());
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

        public Ability getAbility() {
            return new Ability(id, name);
        }
    }
}
