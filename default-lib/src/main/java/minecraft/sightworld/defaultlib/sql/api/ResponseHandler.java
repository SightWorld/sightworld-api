package minecraft.sightworld.defaultlib.sql.api;

public interface ResponseHandler<H, R> {

    R handleResponse(H handle) throws Exception;
}
