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
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoincremental.Activity.EditarGrupoActivity;
import com.example.proyectoincremental.R;
import com.example.proyectoincremental.Utils.Asignatura;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorListaAsignturas extends RecyclerView.Adapter<AdaptadorListaAsignturas.ViewHolder> implements ListAdapter {
    private FirebaseDatabase database;
    private Context context;
    private List<Asignatura> asignaturaList;
    private int layout;
    private List<Asignatura> listaAdinaturasfiltradas;
    private List<String> idAsignaturaUsuarioList = null;
    private AdaptadorListaAsignturas.OnItemClickListener itemClickListener;

    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    private Asignatura asignatura;
    private String id;
    private AlertDialog.Builder builder;
    private String userid;

    //Adaptador para carview eventos con imagen fecha , titulo y numero sitios libres
    public AdaptadorListaAsignturas(List<Asignatura> asignaturas, List<String> idAsignaturaUsuarioList, Context context, int layout, AdaptadorListaAsignturas.OnItemClickListener itemListener) {
        this.asignaturaList = asignaturas;
        this.layout = layout;
        this.context = context;
        this.itemClickListener = itemListener;
        this.idAsignaturaUsuarioList = idAsignaturaUsuarioList;
        this.listaAdinaturasfiltradas = new ArrayList<>(asignaturas);


    }

    @Override
    public AdaptadorListaAsignturas.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        database = FirebaseDatabase.getInstance();
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        context = parent.getContext();
        auth = FirebaseAuth.getInstance();
        AdaptadorListaAsignturas.ViewHolder vh = new AdaptadorListaAsignturas.ViewHolder(v);
        return vh;
    }

    //traemos la informacion de la BBD
    @Override
    public void onBindViewHolder(@NonNull AdaptadorListaAsignturas.ViewHolder holder, final int position) {
        asignatura = asignaturaList.get(position);

        if (idAsignaturaUsuarioList != null && idAsignaturaUsuarioList.size() > 0) {
            for (String a : idAsignaturaUsuarioList) {
                if (a.equals(asignatura.getId())) {

                    holder.checkBox.setChecked(true);
                    //chekbox true defecto
                    //Break para termianr el for
                    break;
                }
            }
        }

        holder.nombre.setText(asignatura.getNombre());
        holder.curso.setText(asignatura.getDescricion());
        holder.bind(asignatura, itemClickListener);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {


        private TextView curso, nombre;
        private CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);

            nombre = (TextView) itemView.findViewById(R.id.name);
            curso = (TextView) itemView.findViewById(R.id.ncurso);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);

            itemView.setOnCreateContextMenuListener(this);
        }

        //clik al item
        public void bind(final Asignatura grupos, final AdaptadorListaAsignturas.OnItemClickListener itemListener) {

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemListener.onItemClick(grupos, getAdapterPosition(), checkBox);
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
                                    //         intent.putExtra("NombreLocal", grupos.get(getAdapterPosition()).getNombreGrupo());
                                    intent.putExtra("Id", asignaturaList.get(getAdapterPosition()).getId());
                                    //       intent.putExtra("Contenido", grupos.get(getAdapterPosition()).getNumeroGrupo());
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
                                    //   intent.putExtra("NombreLocal", grupos.get(getAdapterPosition()).getNombreGrupo());
                                    intent.putExtra("Id", asignaturaList.get(getAdapterPosition()).getId());
                                    // intent.putExtra("Contenido", grupos.get(getAdapterPosition()).getNumeroGrupo());
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
                                    database.getReference("Grupos").child(userid).child(asignaturaList.get(getAdapterPosition()).getId()).removeValue();

                                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                                    dialog.cancel();
                                }
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == DialogInterface.BUTTON_POSITIVE) {
                                    database.getReference("Grupos").child(userid).child(asignaturaList.get(getAdapterPosition()).getId()).removeValue();

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
    public int getItemCount() {
        return asignaturaList.size();
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
        void onItemClick(Asignatura asignatura, int position, CheckBox checkBox);
    }
}
