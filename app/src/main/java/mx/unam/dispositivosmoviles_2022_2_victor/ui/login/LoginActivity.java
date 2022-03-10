package mx.unam.dispositivosmoviles_2022_2_victor.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import mx.unam.dispositivosmoviles_2022_2_victor.R;
import mx.unam.dispositivosmoviles_2022_2_victor.ui.login.LoginViewModel;
import mx.unam.dispositivosmoviles_2022_2_victor.ui.login.LoginViewModelFactory;
import mx.unam.dispositivosmoviles_2022_2_victor.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    // Creando variables nuevas para nuestro edit text,
    // botón, vista de texto y barra de progreso
    private EditText usernameEdt, passwordEdt;
    private Button postDataBtn;
    private TextView responseTV;
    private ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializando nuestras vistas
        usernameEdt = findViewById(R.id.username);
        passwordEdt = findViewById(R.id.password);
        postDataBtn = findViewById(R.id.login);
        responseTV = findViewById(R.id.idTVResponse);
        loadingPB = findViewById(R.id.loading);

        // Agregando listener al boton
        postDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validando si la entrada de texto es vacia o no
                if (usernameEdt.getText().toString().isEmpty() && passwordEdt.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Por favor ingrese email y contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }
                // llamando a un método para publicar los datos y pasando nuestro nombre de usuario y contraseña.
                postDataUsingVolley(usernameEdt.getText().toString(), passwordEdt.getText().toString());
            }
        });
    }

    private void postDataUsingVolley(String username, String password) {
        // Url para publicar nuestros datos
        // Igual se puede testear con la siguiente Url https://reqres.in/api/users
        String url = "https://lamarr.com.mx/webservice2.php";
        loadingPB.setVisibility(View.VISIBLE);

        // creando una nueva variable para nuestra cola de solicitudes
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

        // en la línea de abajo estamos llamando a una cadena
        // a una solicitud para publicar los datos en nuestra API
        // en esto estamos llamando a un método de publicación.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // dentro del método de respuesta estamos
                // escondiendo la barra de progreso
                // y configuración de datos para editar texto como vacío
                loadingPB.setVisibility(View.GONE);
                usernameEdt.setText("");
                passwordEdt.setText("");

                // en la línea de abajo estamos mostrando un mensaje de toast exitoso.
                Toast.makeText(LoginActivity.this, "Datos Agregados a la API :)", Toast.LENGTH_SHORT).show();
                try {
                    // on below line we are passing our response
                    // to json object to extract data from it.
                    JSONObject respObj = new JSONObject(response);

                    // en la línea de abajo estamos pasando nuestra respuesta
                    // extrayendo de nuestro objeto json
                    String username = respObj.getString("username");
                    String password = respObj.getString("password");

                    // en la línea de abajo estamos configurando esta cadena en nuestra vista de texto.
                    responseTV.setText("Email : " + username + "\n" + "Password : " + password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // metodo to manejar errores.
                Toast.makeText(LoginActivity.this, "Error al obtener respuestas = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // debajo de la línea estamos creando un mapa para
                // almacenar valores
                Map<String, String> params = new HashMap<String, String>();

                // en la línea de abajo estamos pasando nuestra key
                // y par de valores a nuestros parámetros.
                params.put("username", username);
                params.put("password", password);


                return params;
            }
        };
        // json object request.
        queue.add(request);
    }
}