package tads.eaj.ufrn.aulamvccrud.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import tads.eaj.ufrn.aulamvccrud.model.Produto;
import tads.eaj.ufrn.aulamvccrud.service.ProdutoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ProdutoController {

    ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @RequestMapping(value = {"/", "/admin", "/admin.html"}, method = RequestMethod.GET)
    public String getAdmin(Model model){

        List<Produto> produtoList = service.findAll();
        model.addAttribute("produtoList", produtoList);
        model.addAttribute("estiloUser", "estilos.css");
        return "admin.html";
    }

    @RequestMapping(value = {"/", "/index", "/index.html"}, method = RequestMethod.GET)
    public String getIndex(Model model){

        List<Produto> produtoList = service.findAll();
        model.addAttribute("produtoList", produtoList);
        model.addAttribute("estiloUser", "estilos.css");
        return "index.html";
    }


    @GetMapping("/cadastrarPage")
    public String getCadastrarPage(Model model){
        Produto p = new Produto();
        model.addAttribute("produto", p);
        return "cadastrarPage";
    }
    @PostMapping("/doSalvar")
    public String doSalvar(@ModelAttribute @Valid Produto p, Errors errors){
        if (errors.hasErrors()){
            return "cadastrarPage";
        }else{
            service.save(p);
            return "redirect:/admin";
            
        }
    }

    @GetMapping("/editarPage/{id}")
    public String getEditarPage(@PathVariable(name = "id") String id, Model model){

        Optional<Produto> p = service.findById(id);
        if (p.isPresent()){
            model.addAttribute("produto", p.get());
        }else{
            return "redirect:/admin";
        }

        return "editarPage";
    }



    @GetMapping("/deletar/{id}")
    public String doDeletar(@PathVariable(name = "id") String id){
        Optional<Produto> produto = service.findById(id);
        if (produto.isPresent()) {
            service.delete(id);
        }
        return "redirect:/admin";
    }
    

    @GetMapping("/adicionarCarrinho/{id}")
    public String adicionarAoCarrinho(@PathVariable(name = "id") String id, HttpSession session) {
        // Recupere o produto com base no ID fornecido
        Optional<Produto> produtoOptional = service.findById(id);
        if (produtoOptional.isPresent()) {
            Produto produto = produtoOptional.get();
            
            // Verifique se já existe um ArrayList "carrinho" na sessão
            List<Produto> carrinho = (List<Produto>) session.getAttribute("carrinho");
            if (carrinho == null) {
                carrinho = new ArrayList<>();
            }
            
            // Adicione o produto ao carrinho
            carrinho.add(produto);
            
            // Atualize o atributo "carrinho" na sessão
            session.setAttribute("carrinho", carrinho);
        }
        
        return "redirect:/index";
    }









}



