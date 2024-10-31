package at.studying;

import at.studying.Request;
import at.studying.Response;

public interface Controller {

    boolean supports(String var1);

    Response handle(Request var1);
}