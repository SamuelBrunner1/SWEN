package at.studying;

import at.studying.Controller;

import at.studying.ServerApplication;
import at.studying.HttpContentType;
import at.studying.HttpStatus;
import at.studying.Request;
import at.studying.Response;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MtcgApp implements ServerApplication {
    private List<Controller> controllers = new ArrayList();

    public MtcgApp() {
        this.controllers.add(new UserController());
    }

    public Response handle(Request request) {
        Iterator var2 = this.controllers.iterator();

        Controller controller;
        do {
            if (!var2.hasNext()) {
                Response response = new Response();
                response.setStatus(HttpStatus.NOT_FOUND);
                response.setContentType(HttpContentType.TEXT_PLAIN);
                response.setBody("Route " + request.getRoute() + " not found in app!");
                return response;
            }

            controller = (Controller)var2.next();
        } while(!controller.supports(request.getRoute()));

        return controller.handle(request);
    }
}
