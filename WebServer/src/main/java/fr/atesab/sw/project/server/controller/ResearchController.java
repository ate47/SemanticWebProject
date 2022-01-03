package fr.atesab.sw.project.server.controller;

import fr.atesab.sw.project.territoire.TerritoireScraper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/research")

public class ResearchController {

    public ResearchController() {
        //Récuperer ici les résultats grace au scrapper et les mettre dans une liste
        /* private List <territoire> territoireList; */
        /* territoireList.add(...)  */
    }


    @GetMapping("home")
    public String home(){
        return "Hello this is home page";
    }

   /* @GetMapping("/home/{id}")
    public TerritoireScraper get(@PathVariable String id){
        return territoireList.stream().filter(t -> id.equals(t.getId()))/findAny().orElse(null)
    }
    */
}
