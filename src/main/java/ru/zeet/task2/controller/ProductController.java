package ru.zeet.task2.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.zeet.task2.entity.Product;
import ru.zeet.task2.entity.ProductSite;
import ru.zeet.task2.service.ProductService;

import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping
public class ProductController {
    private static Logger log = Logger.getLogger(ProductController.class.getName());

    private ProductService service;

    @Autowired
    public void setNoteService(ProductService service) {
        this.service = service;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {
        return new ModelAndView("index");
    }


    @RequestMapping(value = "/get", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public List<Product> testJSON(@RequestBody String parent)  {
        Integer parentId = Integer.parseInt(parent);
        // String p = parent.replaceAll("\"", "");
        if (parentId == 0) {
            parentId = null;
        }
        // else { parentId = Integer.parseInt(p); }
        //try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
        return service.findAllByParentId(parentId);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ProductSite deleteElement(@RequestBody ProductSite productSite) {
        service.deleteSite(productSite);

        String response = "";
        return productSite;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ProductSite add(@RequestBody ProductSite productSite) {
        return service.addSite(productSite);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ProductSite update(@RequestBody ProductSite productSite) {
        if (isDigit(productSite.getId())) {
            return service.updateSite(productSite);
        } else {
            return service.addSite(productSite);
        }


    }

    private boolean isDigit(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }



}