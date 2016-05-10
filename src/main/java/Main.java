import com.sun.org.apache.xpath.internal.operations.Mod;
import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

import static spark.Spark.halt;

/**
 * main class for gametracker
 */
public class Main {

    public static void main(String[] args){

        Spark.get(
                "/",
                (request, response) -> {
                    HashMap m = new HashMap();

                    if(!request.session().attributes().contains("user")){
                        //show login
                        return new ModelAndView(m, "login.mustache");

                    }else{
                        //show home
                        User user = request.session().attribute("user");
                        //user.games.add(new Game("Mario Bros", "platformer", "NES", 1987));
                        m.put("user",user);

                        return new ModelAndView(m, "home.mustache");

                    }


                },
                new MustacheTemplateEngine()
        );
        Spark.post(
                "/login",
                (request, response) -> {
                    if (request.queryParams("loginName").trim().length() > 0) {
                        request.session().attribute("user", new User(request.queryParams("loginName")));
                        response.redirect("/");
                        halt();
                    }
                    response.redirect("/");
                    halt();

                    return "";
                }

        );
        Spark.get(
                "/logout",
                (request, response) -> {
                    request.session().invalidate();
                    response.redirect("/");
                    halt();

                    return "";
                }
        );
        Spark.get(
                "/create-game",
                (request, response) -> {
                    HashMap m = new HashMap();

                    if(!request.session().attributes().contains("user")){
                        //show login
                        response.redirect("/");
                        halt();

                        return null;

                    }else{
                        //show game form
                        return new ModelAndView(m, "gameForm.mustache");
                    }

                },
                new MustacheTemplateEngine()

        );
                    Spark.post(
                            "/create-game",
                            (request, response) -> {
                                //show login
                                if(!request.session().attributes().contains("user")){
                                    response.redirect("/");
                                    halt();
                                }else{
                                    //create the game and add to the user
                                    try {
                                        Game game = new Game(
                                                request.queryParams("gameName"),
                                                request.queryParams("gameGenre"),
                                                request.queryParams("gamePlatform"),
                                                Integer.valueOf(request.queryParams("gameYear"))
                                        );

                                        User user = request.session().attribute("user");
                                        user.games.add(game);
                                        response.redirect("/");
                                        halt();


                                    }catch (NumberFormatException e){
                                        HashMap m = new HashMap();
                                        m.put("error", "there was a prob with your release yr.");
                                        m.put("gameName", request.queryParams("gameName"));
                                        m.put("gameGenre", request.queryParams("gameGenre"));
                                        m.put("gamePlatform", request.queryParams("gamePLatform"));
                                        m.put("gameYear", request.queryParams("gameYear"));
                                        return new ModelAndView(m, "gameForm.mustache");
                                    }


                                }
                                return null;

                            },
                            new MustacheTemplateEngine()

                    );
    }


}
