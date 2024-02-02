package at.qe.skeleton.external.exceptions;

public class WeatherApiException extends RuntimeException{

    public WeatherApiException(String message) {
        super(message);
    }
}
