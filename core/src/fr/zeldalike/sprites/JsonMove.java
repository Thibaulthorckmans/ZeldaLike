package fr.zeldalike.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Json;

public class JsonMove {

	public static void sendRequest(Body body) {
		String method = "POST";
		Position requestObject = new Position();
		requestObject.setX(body.getPosition().x);
		requestObject.setY(body.getPosition().y);

		final Json json = new Json();

		String requestJson = json.toJson(requestObject); // this is just an example

		Net.HttpRequest request = new Net.HttpRequest(method);


		final String url = "137.74.45.154";


		request.setUrl(url);

		request.setContent(requestJson);

		request.setHeader("Content-Type", "application/json");
		request.setHeader("Accept", "application/json");

		Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {

			@Override
			public void handleHttpResponse(Net.HttpResponse httpResponse) {

				int statusCode = httpResponse.getStatus().getStatusCode();
				if(statusCode != HttpStatus.SC_OK) {
					System.out.println("Request Failed");
					return;
				}

				String responseJson = httpResponse.getResultAsString();
				try {


					//DO some stuff with the response string

				}
				catch(Exception exception) {

					exception.printStackTrace();
				}
			}

			@Override
			public void failed(Throwable t) {
				//System.out.println("Request Failed Completely");
			}

			@Override
			public void cancelled() {
				System.out.println("request cancelled");

			}

		});

	}
}

