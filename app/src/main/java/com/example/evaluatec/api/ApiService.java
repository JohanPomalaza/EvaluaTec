package com.example.evaluatec.api;
import com.example.evaluatec.modelos.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
public interface ApiService {
    @POST("api/Login/login") //este es la ruta de la api
    Call<LoginResponse> login(@Body Usuario usuario);
}
