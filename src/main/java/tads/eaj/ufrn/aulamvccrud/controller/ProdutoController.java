package tads.eaj.ufrn.aulamvccrud.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import tads.eaj.ufrn.aulamvccrud.model.Produto;
import tads.eaj.ufrn.aulamvccrud.service.ProdutoService;
import tads.eaj.ufrn.aulamvccrud.util.Upload;
import tads.eaj.ufrn.aulamvccrud.repository.ProdutoRepository;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class ProdutoController {

    ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @RequestMapping(value = {"/admin", "/admin.html"}, method = RequestMethod.GET)
    public String getAdmin(Model model){

        List<Produto> produtoList = service.findAll();
        model.addAttribute("produtoList", produtoList);
        model.addAttribute("estiloUser", "estilos.css");
        return "admin.html";
    }

    @RequestMapping(value = {"/index", "/index.html"}, method = RequestMethod.GET)
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


    /* 
    @PostMapping("/doSalvar")
    public String doSalvar(@ModelAttribute @Valid Produto p, Errors errors){
        if (errors.hasErrors()){
            return "cadastrarPage";
        }else{
            service.save(p);
            return "redirect:/admin";
            
        }
    }

    */


    @Autowired
    private ProdutoRepository repo;
     
    @PostMapping("/doSalvar")
    public RedirectView saveProduto(Produto produto, @RequestParam("image") MultipartFile multipartFile) throws IOException {
         
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        produto.setImageURI(fileName);
         
        Produto savedProduto = repo.save(produto);
 
        String uploadDir = "user-photos/" + savedProduto.getId();
 
        Upload.saveFile(uploadDir, fileName, multipartFile);
         
        return new RedirectView("/admin", true);
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
    

    /*
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

    */






    
    @GetMapping("/adicionarCarrinho/{id}")
    public void adicionarAoCarrinho(@PathVariable(name = "id") String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        List<Produto> carrinho = (List<Produto>) session.getAttribute("carrinho");
        if (carrinho == null) {
            carrinho = new ArrayList<>();
        }
        Optional<Produto> produtoOptional = service.findById(id);
        if (produtoOptional.isPresent()) {
            Produto produto = produtoOptional.get();
            carrinho.add(produto);
        }
        session.setAttribute("carrinho", carrinho);
        response.sendRedirect("/index");
    }



    @GetMapping("/carrinhoPage")
    public String exibirCarrinho(Model model, HttpServletRequest request) {
    HttpSession session = request.getSession();
    List<Produto> carrinho = (List<Produto>) session.getAttribute("carrinho");

    model.addAttribute("carrinho", carrinho);

    return "carrinhoPage";
    }

    @GetMapping("/finalizarCompra")
    public String finalizarCompra(HttpServletRequest request) {
    HttpSession session = request.getSession();
    
    // Invalida a sessão existente
    session.invalidate();
    
    // Redireciona para a página "index"
    return "redirect:/index";
    }





}



