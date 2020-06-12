package com.example.proyectoincremental.Adaptadores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoincremental.Activity.EditarAsignaturaActivity;
import com.example.proyectoincremental.Activity.EditarGrupoActivity;
import com.example.proyectoincremental.R;
import com.example.proyectoincremental.Utils.Asignatura;
import com.example.proyectoincremental.Utils.Grupos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorGrupos extends RecyclerView.Adapter<AdaptadorGrupos.ViewHolder> implements ListAdapter, Filterable {
    private FirebaseDatabase database;
    private Context context;
    private List<Grupos> grupos;
    private int layout;
    private List<Grupos> listaAdinaturasfiltradas;

    private AdaptadorGrupos.OnItemClickListener itemClickListener;
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    private Grupos grupo;
    private String id;
    private AlertDialog.Builder builder;
    private String userid;

    //Adaptador para carview eventos con imagen fecha , titulo y numero sitios libres
    public AdaptadorGrupos(List<Grupos> asignaturas, Context context, int layout, AdaptadorGrupos.OnItemClickListener itemListener) {
        this.grupos = asignaturas;
        this.layout = layout;
        this.context = context;
        this.itemClickListener = itemListener;
        this.listaAdinaturasfiltradas = new ArrayList<>(asignaturas);
    }

    @Override
    public AdaptadorGrupos.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        database = FirebaseDatabase.getInstance();
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        context = parent.getContext();
        auth = FirebaseAuth.getInstance();
        AdaptadorGrupos.ViewHolder vh = new AdaptadorGrupos.ViewHolder(v);
        return vh;
    }

    //traemos la informacion de la BBD
    @Override
    public void onBindViewHolder(@NonNull AdaptadorGrupos.ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        grupo = grupos.get(position);
        holder.nombre.setText(grupo.getNombreGrupo());
        holder.curso.setText(grupo.getNumeroGrupo());

        //preguntar a agus si lo cambiamos a string par atraerlo mas facil
        //holder.plazas.setText(eventoss.getFecha());
        holder.bind(grupo, itemClickListener);


    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {


        public TextView curso, nombre;

        public ViewHolder(View itemView) {
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.texviewNombregrupo);
            curso = (TextView) itemView.findViewById(R.id.texviewnumeroCurso);
            itemView.setOnCreateContextMenuListener(this);


        }

        //clik al item
        public void bind(final Grupos grupos, final AdaptadorGrupos.OnItemClickListener itemListener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemListener.onItemClick(grupos, getAdapterPosition());
                }
            });

        }



        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem Edit = menu.add(Menu.NONE, 1, 1, "Editar");
            MenuItem Delete = menu.add(Menu.NONE, 2, 2, "Eliminar");
            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);
        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FirebaseUser firebaseUser = auth.getCurrentUser();

                userid = firebaseUser.getUid();
                switch (item.getItemId()) {
                    case 1:

                        builder = new AlertDialog.Builder(context);
                        builder.setTitle("ATENCION");
                        builder.setMessage("\n" + "Seguro que quieres Editar");
                        // Set up the buttons
                        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (which == DialogInterface.BUTTON_POSITIVE) {
                                    Intent intent = new Intent(context, EditarGrupoActivity.class);
                                    intent.putExtra("NombreLocal", grupos.get(getAdapterPosition()).getNombreGrupo());
                                    intent.putExtra("Id", grupos.get(getAdapterPosition()).getId());
                                    intent.putExtra("Contenido", grupos.get(getAdapterPosition()).getNumeroGrupo());
                                    context.startActivity(intent);
                                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                                    dialog.cancel();
                                }
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == DialogInterface.BUTTON_POSITIVE) {
                                    Intent intent = new Intent(context, EditarGrupoActivity.class);
                                    intent.putExtra("NombreLocal", grupos.get(getAdapterPosition()).getNombreGrupo());
                                    intent.putExtra("Id", grupos.get(getAdapterPosition()).getId());
                                    intent.putExtra("Contenido", grupos.get(getAdapterPosition()).getNumeroGrupo());
                                    context.startActivity(intent);
                                    context.startActivity(intent);
                                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                                    dialog.cancel();
                                }
                            }
                        });
                        builder.show();


                        break;

                    case 2:

                        builder = new AlertDialog.Builder(context);
                        builder.setTitle("ATENCION");
                        builder.setMessage("\n" + "Seguro que quieres Editar");
                        // Set up the buttons
                        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (which == DialogInterface.BUTTON_POSITIVE) {
                                    database.getReference("Grupos").child(userid).child(grupos.get(getAdapterPosition()).getId()).removeValue();

                                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                                    dialog.cancel();
                                }
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == DialogInterface.BUTTON_POSITIVE) {
                                    database.getReference("Grupos").child(userid).child(grupos.get(getAdapterPosition()).getId()).removeValue();

                                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                                    dialog.cancel();
                                }
                            }
                        });
                        builder.show();
                        break;
                }
                return true;
            }
        };
    }
    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Grupos> filtrada = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filtrada.addAll(listaAdinaturasfiltradas);
            } else {
                String letras = constraint.toString().toLowerCase().trim();
                for (Grupos grupos : listaAdinaturasfiltradas) {
                    if (grupos.getNombreGrupo().toLowerCase().contains(letras)) {
                        filtrada.add(grupos);
                    }
                }

            }
            FilterResults results = new FilterResults();
            results.values = filtrada;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            grupos.clear();
            grupos.addAll((List) results.values);
            notifyDataSetChanged();
        }

    };

    @Override
    public int getItemCount() {
        return grupos.size();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }

    public interface OnItemClickListener {
        void onItemClick(Grupos asignatura, int position);
    }


}
