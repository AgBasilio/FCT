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
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoincremental.Activity.EditarAsignaturaActivity;
import com.example.proyectoincremental.R;
import com.example.proyectoincremental.Utils.Asignatura;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AdaptadorAsignaturas extends RecyclerView.Adapter<AdaptadorAsignaturas.ViewHolder> implements ListAdapter , Filterable {
    private FirebaseDatabase database;
    private Context context;
    private List<Asignatura> asignaturas;
    private List<Asignatura>listaAdinaturasfiltradas;
    private int layout;
    private AdaptadorAsignaturas.OnItemClickListener itemClickListener;
    private FirebaseAuth auth;
    private Asignatura asignatura;
    private String id;
    private AlertDialog.Builder builder;
    String userid;
    public static final String URL_FOTO_USRr = "https://firebasestorage.googleapis.com/v0/b/bbdafanigths.appspot.com/o/fotosNormales%2F2694-1.jpg?alt=media&token=522e3e15-ff17-46c5-9dbc-0379af57871c";
    private String URL_FOTO_USRs;

    //Adaptador para carview eventos con imagen fecha , titulo y numero sitios libres
    public AdaptadorAsignaturas(List<Asignatura> asignaturas, Context context, int layout, OnItemClickListener itemListener) {
        this.asignaturas = asignaturas;
        this.layout = layout;
        this.context = context;
        this.itemClickListener = itemListener;
        this.listaAdinaturasfiltradas=new ArrayList<>(asignaturas);

    }


    @Override
    public AdaptadorAsignaturas.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        database = FirebaseDatabase.getInstance();
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        context = parent.getContext();
        auth = FirebaseAuth.getInstance();
        AdaptadorAsignaturas.ViewHolder vh = new AdaptadorAsignaturas.ViewHolder(v);
        return vh;
    }

    //traemos la informacion de la BBD
    @Override
    public void onBindViewHolder(@NonNull AdaptadorAsignaturas.ViewHolder holder, int position) {
        asignatura = asignaturas.get(position);


        holder.nombre.setText(asignatura.getNombre());
        holder.curso.setText(asignatura.getCurso());
        holder.bind(asignatura, itemClickListener);
        if (!asignatura.getImgAsignatura().isEmpty() && URLUtil.isValidUrl(asignatura.getImgAsignatura())) {

            Picasso.get().load(asignatura.getImgAsignatura()).resize(540, 550).centerCrop().into(holder.img);
        } else {

            URL_FOTO_USRs = "https://firebasestorage.googleapis.com/v0/b/proyecto-fct-83b84.appspot.com/o/cuenta.png?alt=media&token=9b30a70e-28c2-4e29-be65-18c599d09ffb";
            Picasso.get().load(URL_FOTO_USRs).resize(540, 550).centerCrop().into(holder.img);

        }
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {


        public TextView curso, nombre;
        public ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.texviewNombre);
            curso = (TextView) itemView.findViewById(R.id.texviewCurso);
            img = (ImageView) itemView.findViewById(R.id.imageViewCity);


            itemView.setOnCreateContextMenuListener(this);


        }

        //clik al item
        public void bind(final Asignatura asignaturas, final AdaptadorAsignaturas.OnItemClickListener itemListener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemListener.onItemClick(asignaturas, getAdapterPosition());
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
                                    Intent intent = new Intent(context, EditarAsignaturaActivity.class);
                                    intent.putExtra("NombreAsignatura", asignaturas.get(getAdapterPosition()).getNombre());
                                    intent.putExtra("Curso", asignaturas.get(getAdapterPosition()).getCurso());
                                    intent.putExtra("Descripcion", asignaturas.get(getAdapterPosition()).getDescricion());
                                    intent.putExtra("Img", asignaturas.get(getAdapterPosition()).getImgAsignatura());
                                    intent.putExtra("Id", asignaturas.get(getAdapterPosition()).getId());
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
                                    Intent intent = new Intent(context, EditarAsignaturaActivity.class);
                                    intent.putExtra("NombreAsignatura", asignaturas.get(getAdapterPosition()).getNombre());
                                    intent.putExtra("Curso", asignaturas.get(getAdapterPosition()).getCurso());
                                    intent.putExtra("Descripcion", asignaturas.get(getAdapterPosition()).getDescricion());
                                    intent.putExtra("Img", asignaturas.get(getAdapterPosition()).getImgAsignatura());
                                    intent.putExtra("Id", asignaturas.get(getAdapterPosition()).getId());
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
                        builder.setMessage("\n" + "Seguro que quieres ELIMINAR ");
                        // Set up the buttons
                        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (which == DialogInterface.BUTTON_POSITIVE) {
                                    database.getReference("Asignaturas").child(userid).child(asignaturas.get(getAdapterPosition()).getId()).removeValue();

                                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                                    dialog.cancel();
                                }
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == DialogInterface.BUTTON_POSITIVE) {
                                    database.getReference("Asignaturas").child(userid).child(asignaturas.get(getAdapterPosition()).getId()).removeValue();

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
            List<Asignatura> filtrada = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filtrada.addAll(listaAdinaturasfiltradas);
            } else {
                String letras = constraint.toString().toLowerCase().trim();
                for (Asignatura asignatura : listaAdinaturasfiltradas) {
                    if (asignatura.getNombre().toLowerCase().contains(letras)) {
                        filtrada.add(asignatura);
                    }
                }

            }
            FilterResults results = new FilterResults();
            results.values = filtrada;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
           asignaturas.clear();
            asignaturas.addAll((List)results.values);
            notifyDataSetChanged();
        }

    };

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
        void onItemClick(Asignatura asignatura, int position);
    }


}
