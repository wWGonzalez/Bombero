package app.keepthink.user.KeepThink.interfaces;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wer.KeepThink.R;

public class SliderAdapter extends PagerAdapter{

    Context context;
    LayoutInflater  layoutInflater;
    public SliderAdapter(Context context){

        this.context = context;
    }
    public int[] slide_images = {
            R.drawable.tuto_maternidad,
            R.drawable.tuto_accidentes,
            R.drawable.tuto_incendios,
            R.drawable.tuto_primeros_auxilios
    };
    public String[] slide_headings = {
            "MATERNIDAD",
            "ACCIDENTES",
            "INCENDIOS",
            "PRIMEROS AUXILIOS"
    };
    public String[] slide_descs = {
            "Envíe alertas para casos de maternidad de una manera muy sencilla y rápida.",
            "Si necesita enviar una alerta relacionada a un accidente, puede enviarla de forma rápida presionando el ícono y le atenderemos lo más pronto posible.",
            "En casos de incendios solo debe presionar el botón correspondiente a Incendios y la alerta se envía automáticamente para que pueda ser atendida.",
            "En caso de que necesite primeros auxilios sólo debe presionar el botón de Primeros Auxilios y de inmediato tendremos los datos para poder atenderlo."
    };
    @Override
    public int getCount() {
        return slide_headings.length;
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position){
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.slide_layout, container,false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.imgView);
        TextView slideHeading =(TextView) view.findViewById(R.id.txtTitulo);
        TextView slideDescription =(TextView) view.findViewById(R.id.txtDetalles);
        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_descs[position]);
        container.addView(view);
        return view;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((RelativeLayout)object);
    }
}