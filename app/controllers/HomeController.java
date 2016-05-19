package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import uk.nickbdyer.tictactoe.Board;
import views.html.game;
import views.html.index;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public Result game() {
        return ok(game.render("Let's Play!"));
    }

    public Result newgame() {
        return redirect("/game");
    }

    public Result makeMove() {
        JsonNode json = Json.toJson(new Board().getCells());
        return ok(json);
    }

}
