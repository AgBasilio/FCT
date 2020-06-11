package com.example.proyectoincremental.Adaptadores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoincremental.Activity.EditarGrupoActivity;
import com.example.proyectoincremental.R;
import com.example.proyectoincremental.Utils.Asignatura;
import com.example.proyectoincremental.Utils.Reuniones;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.proyectoincremental.Adaptadores.AdaptadorAsignaturas.URL_FOTO_USRr;

public class AdaptadorReuniones extends RecyclerView.Adapter<AdaptadorReuniones.ViewHolder> implements ListAdapter {
    private FirebaseDatabase database;
    private Context context;
    protected List<Asignatura> asignaturas;
    private List<Reuniones> listaReuniones;

    protected int layout;
    private List<Asignatura> listaAdinaturasfiltradas;
    private String[] asignaturasusuario = null;
    private AdaptadorReuniones.OnItemClickListener itemClickListener;
    private CardView cardView;

    private RecyclerView recyclerView, recyclerView1;

    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    protected Asignatura asignatura;
    private String id;
    private AlertDialog.Builder builder;
    private String userid;

    //Adaptador para carview eventos con imagen fecha , titulo y numero sitios libres
    public AdaptadorReuniones(List<Reuniones> listareuniones, List<Asignatura> asignaturas, Context context, int layout, AdaptadorReuniones.OnItemClickListener itemListener) {
        this.asignaturas = asignaturas;
        this.listaReuniones = listareuniones;
        this.layout = layout;
        this.context = context;
        this.itemClickListener = itemListener;
        this.listaAdinaturasfiltradas = new ArrayList<>(asignaturas);


    }

    @Override
    public AdaptadorReuniones.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        database = FirebaseDatabase.getInstance();
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.listarasignaturasparareunion);

        context = parent.getContext();
        auth = FirebaseAuth.getInstance();
        AdaptadorReuniones.ViewHolder vh = new AdaptadorReuniones.ViewHolder(v);
        return vh;
    }

    //traemos la informacion de la BBD
    @Override
    public void onBindViewHolder(@NonNull AdaptadorReuniones.ViewHolder holder, final int position) {
        /**/

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        asignatura = asignaturas.get(position);
        Asignatura asignatura = asignaturas.get(position);

        //por defecto a blanco
        ((CardView) holder.itemView).setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        holder.bind(null, itemClickListener);

        if (listaReuniones != null && listaReuniones.size() > 0) {
            //buscar en lista de reuniones si esta esta asignatura
            for (Reuniones r : listaReuniones) {
                if (r.getAsignarra().equals(asignatura.getId())) {
                    //si la encontro, la pinto verde y dejo de buscar
                    ((CardView) holder.itemView).setCardBackgroundColor(Color.parseColor("#2d572c"));
                    //para reservas
                    holder.bind(r, itemClickListener);
                    //--
                    break;
                }
            }
        }

        holder.nombre.setText(this.asignatura.getNombre());
        holder.curso.setText(this.asignatura.getDescricion());

        if (!this.asignatura.getImgAsignatura().isEmpty()) {

            Picasso.get().load(this.asignatura.getImgAsignatura()).resize(540, 550).centerCrop().into(holder.img);
        } else {
            Picasso.get().load(URL_FOTO_USRr).resize(540, 450).centerCrop().into(holder.img);
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {


        protected TextView curso;
        protected TextView nombre;
        public ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);

            nombre = (TextView) itemView.findViewById(R.id.texviewNombre);
            curso = (TextView) itemView.findViewById(R.id.texviewCurso);
            img = (ImageView) itemView.findViewById(R.id.imageViewCity);

            itemView.setOnCreateContextMenuListener(this);
        }

        //clik al item
        public void bind(final Reuniones reuniones, final AdaptadorReuniones.OnItemClickListener itemListener) {


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //  itemView.setBackgroundColor(Color.parseColor("#000000"));
                    itemListener.onItemClick(reuniones, getAdapterPosition(),itemView);
                }
            });

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//            MenuItem Edit = menu.add(Menu.NONE, 1, 1, "Edit");
//            MenuItem Delete = menu.add(Menu.NONE, 2, 2, "Delete");
//            Edit.setOnMenuItemClickListener(onEditMenu);
//            Delete.setOnMenuItemClickListener(onEditMenu);
        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                FirebaseUser firebaseUser = auth.getCurrentUser();
//
//                userid = firebaseUser.getUid();
//                switch (item.getItemId()) {
//                    case 1:
//
//                        builder = new AlertDialog.Builder(context);
//                        builder.setTitle("ATENCION");
//                        builder.setMessage("\n" + "Seguro que quieres Editar");
//                        // Set up the buttons
//                        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                if (which == DialogInterface.BUTTON_POSITIVE) {
//                                    Intent intent = new Intent(context, EditarGrupoActivity.class);
//                                    //         intent.putExtra("NombreLocal", grupos.get(getAdapterPosition()).getNombreGrupo());
//                                    intent.putExtra("Id", asignaturas.get(getAdapterPosition()).getId());
//                                    //       intent.putExtra("Contenido", grupos.get(getAdapterPosition()).getNumeroGrupo());
//                                    context.startActivity(intent);
//                                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
//                                    dialog.cancel();
//                                }
//                            }
//                        });
//                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                if (which == DialogInterface.BUTTON_POSITIVE) {
//                                    Intent intent = new Intent(context, EditarGrupoActivity.class);
//                                    //   intent.putExtra("NombreLocal", grupos.get(getAdapterPosition()).getNombreGrupo());
//                                    intent.putExtra("Id", asignaturas.get(getAdapterPosition()).getId());
//                                    // intent.putExtra("Contenido", grupos.get(getAdapterPosition()).getNumeroGrupo());
//                                    context.startActivity(intent);
//                                    context.startActivity(intent);
//                                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
//                                    dialog.cancel();
//                                }
//                            }
//                        });
//                        builder.show();
//
//
//                        break;
//
//                    case 2:
//
//                        builder = new AlertDialog.Builder(context);
//                        builder.setTitle("ATENCION");
//                        builder.setMessage("\n" + "Seguro que quieres Editar");
//                        // Set up the buttons
//                        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                if (which == DialogInterface.BUTTON_POSITIVE) {
//                                    database.getReference("Grupos").child(userid).child(asignaturas.get(getAdapterPosition()).getId()).removeValue();
//
//                                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
//                                    dialog.cancel();
//                                }
//                            }
//                        });
//                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                if (which == DialogInterface.BUTTON_POSITIVE) {
//                                    database.getReference("Grupos").child(userid).child(asignaturas.get(getAdapterPosition()).getId()).removeValue();
//
//                                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
//                                    dialog.cancel();
//                                }
//                            }
//                        });
//                        builder.show();
//                        break;
//                }
                return true;
            }
        };
    }


    @Override
    public int getItemCount() {
        return asignaturas.size();
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
        void onItemClick(Reuniones reunion, int position, View itemView);
    }


}
