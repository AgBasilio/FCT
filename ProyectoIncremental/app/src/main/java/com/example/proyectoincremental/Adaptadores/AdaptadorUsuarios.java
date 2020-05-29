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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoincremental.Activity.CrearUsuarioActivity;
import com.example.proyectoincremental.Activity.EditarGrupoActivity;
import com.example.proyectoincremental.R;
import com.example.proyectoincremental.Utils.Grupos;
import com.example.proyectoincremental.Utils.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.proyectoincremental.Adaptadores.AdaptadorListaAsignturas.URL_FOTO_USRr;

public class AdaptadorUsuarios extends RecyclerView.Adapter<AdaptadorUsuarios.ViewHolder> implements ListAdapter, Filterable {
    private Context context;
    private List<Usuario> usuarios;
    private int layout;
    private List<Usuario> listaAdinaturasfiltradas;
    private AdaptadorUsuarios.OnItemClickListener itemClickListener;

    private DatabaseReference databsaserefernece;
    private FirebaseDatabase database;
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    private Usuario usuario;
    private String id;
    private AlertDialog.Builder builder;
    private String userid;

    //Adaptador para carview eventos con imagen fecha , titulo y numero sitios libres
    public AdaptadorUsuarios(List<Usuario> usuarios, Context context, int layout, AdaptadorUsuarios.OnItemClickListener itemListener) {
        this.usuarios = usuarios;
        this.layout = layout;
        this.context = context;
        this.itemClickListener = itemListener;
        this.listaAdinaturasfiltradas = new ArrayList<>(usuarios);
    }

    @Override
    public AdaptadorUsuarios.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        database = FirebaseDatabase.getInstance();
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        context = parent.getContext();
        auth = FirebaseAuth.getInstance();
        AdaptadorUsuarios.ViewHolder vh = new AdaptadorUsuarios.ViewHolder(v);
        return vh;
    }

    //traemos la informacion de la BBD
    @Override
    public void onBindViewHolder(@NonNull AdaptadorUsuarios.ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        usuario = usuarios.get(position);
        holder.nombre.setText(usuario.getNombre());
        holder.tipo.setText(usuario.getTipo());
        if (!usuario.getImagen().isEmpty()) {
            Picasso.get().load(usuario.getImagen()).resize(540, 550).centerCrop().into(holder.imgusuario);
        } else {

            Picasso.get().load(URL_FOTO_USRr).resize(540, 450).centerCrop().into(holder.imgusuario);

        }
        holder.bind(usuario, itemClickListener);


    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {


        public TextView nombre, tipo;
        ImageView imgusuario;

        public ViewHolder(View itemView) {
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.nombreapellidousuario);
            tipo = (TextView) itemView.findViewById(R.id.tipodeusuario);
            imgusuario = (ImageView) itemView.findViewById(R.id.imagenusuario);

            itemView.setOnCreateContextMenuListener(this);


        }

        //clik al item
        public void bind(final Usuario usuario, final AdaptadorUsuarios.OnItemClickListener itemListener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemListener.onItemClick(usuario, getAdapterPosition());
                }
            });

        }


        //menu de editar y eliminar

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem Edit = menu.add(Menu.NONE, 1, 1, "Edit");
            MenuItem Delete = menu.add(Menu.NONE, 2, 2, "Delete");
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
                                    Intent intent = new Intent(context, CrearUsuarioActivity.class);
                                    intent.putExtra("NombreLocal", usuarios.get(getAdapterPosition()).getNombre());
                                    intent.putExtra("Id", usuarios.get(getAdapterPosition()).getId());
                                    intent.putExtra("Contenido", usuarios.get(getAdapterPosition()).getEdad());
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
                                    //intent.putExtra("NombreLocal", usuarios.get(getAdapterPosition()).getNombreGrupo());
                                    //intent.putExtra("Id", usuarios.get(getAdapterPosition()).getId());
                                    //intent.putExtra("Contenido", usuarios.get(getAdapterPosition()).getNumeroGrupo());
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
                        builder.setMessage("\n" + "Seguro que quieres eliminar este usuario?");
                        // Set up the buttons
                        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (which == DialogInterface.BUTTON_POSITIVE) {
                                    databsaserefernece = database.getReference("Usuarios").child(usuarios.get(getAdapterPosition()).getId());
                                    databsaserefernece.removeValue();
                                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                                    dialog.cancel();
                                }

                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == DialogInterface.BUTTON_POSITIVE) {
                                    databsaserefernece = database.getReference("Usuarios").child(usuarios.get(getAdapterPosition()).getId());

                                    databsaserefernece.removeValue();
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

    //filtro de busqueda inplementado en el toolbar uno por fragemnto aun falta par el tercero 4 semana
    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Usuario> filtrada = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filtrada.addAll(listaAdinaturasfiltradas);
            } else {
                String letras = constraint.toString().toLowerCase().trim();
                for (Usuario usuario : listaAdinaturasfiltradas) {
                    if (usuario.getNombre().toLowerCase().contains(letras)) {
                        filtrada.add(usuario);
                    }
                }

            }
            FilterResults results = new FilterResults();
            results.values = filtrada;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            usuarios.clear();
            usuarios.addAll((List) results.values);
            notifyDataSetChanged();
        }

    };

    @Override
    public int getItemCount() {
        return usuarios.size();
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
        void onItemClick(Usuario usuario, int position);
    }


}

