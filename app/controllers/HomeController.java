package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import uk.nickbdyer.tictactoe.Board;
import views.html.game;
import views.html.index;

import java.util.Arrays;

import static uk.nickbdyer.tictactoe.Mark.EMPTY;
import static uk.nickbdyer.tictactoe.Mark.O;
import static uk.nickbdyer.tictactoe.Mark.X;

public class HomeController extends Controller {

    public Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public Result game() {
        return ok(game.render("Let's Play!", Arrays.asList(X, O, X, EMPTY, X, O, EMPTY, EMPTY, X)));
    }

    public Result newgame() {
        return redirect("/game");
    }

    public Result makeMove() {
        JsonNode json = Json.toJson(new Board().getCells());
        return ok(json);
    }

}
