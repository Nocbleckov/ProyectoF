package com.baytag.daniel.proyectof.adaptadores;

import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baytag.daniel.proyectof.R;
import com.baytag.daniel.proyectof.interfaces.LongClickListener;
import com.baytag.daniel.proyectof.objetos.Contacto;

import java.util.ArrayList;

/**
 * Created by Daniel on 26/08/2017.
 */

public class ContactosAdapter extends RecyclerView.Adapter<ContactosAdapter.ContactoViewHolder> {

    ArrayList<Contacto> contactos;
    Contacto contacto;

    public ContactosAdapter(ArrayList<Contacto> contactos) {
        this.contactos = contactos;
    }


    @Override
    public ContactoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contacto, parent, false);
        ContactoViewHolder cVh = new ContactoViewHolder(v);

        return cVh;
    }

    @Override
    public void onBindViewHolder(ContactoViewHolder holder, final int position) {
        holder.txVwTelefono.setText(contactos.get(position).getTelCon());
        holder.txVwNombreCon.setText(contactos.get(position).getNomCon());
        holder.txVwNombreCd.setText(contactos.get(position).getCiudadCon());
        holder.txVwCorreo.setText(contactos.get(position).getCorreoCon());
        holder.setLongClickListener(new LongClickListener() {
            @Override
            public void onItemLongClick(int Pos) {
                contacto = contactos.get(Pos);
            }
        });
    }

    public Contacto getItemSelected(){
        return this.contacto;
    }

    @Override
    public int getItemCount() {
        return contactos.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ContactoViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener,View.OnCreateContextMenuListener{

        TextView txVwNombreCon, txVwNombreCd, txVwTelefono, txVwCorreo;
        LongClickListener longClickListener;

        public ContactoViewHolder(final View itemView) {
            super(itemView);

            txVwCorreo = (TextView) itemView.findViewById(R.id.txVw_EmComCon_adpCon);
            txVwNombreCd = (TextView) itemView.findViewById(R.id.txVw_NomCdCon_adpCon);
            txVwNombreCon = (TextView) itemView.findViewById(R.id.txVw_NomComCon_adpCon);
            txVwTelefono = (TextView) itemView.findViewById(R.id.txVw_TelComCon_adpCon);

            itemView.setOnLongClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        public void setLongClickListener(LongClickListener longClickListener) {
            this.longClickListener = longClickListener;
        }

        @Override
        public boolean onLongClick(View v) {
            this.longClickListener.onItemLongClick(getLayoutPosition());
            return false;
        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(R.string.llamar);
            menu.add(R.string.escribir_email);
            menu.add(R.string.editar);
            menu.add(R.string.eliminar);
        }
    }

}
