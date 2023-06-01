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
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import tads.eaj.ufrn.aulamvccrud.model.Produto;
import tads.eaj.ufrn.aulamvccrud.model.Usuario;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@Controller
public class ProdutoController {

    ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @RequestMapping(value = {"/admin", "/admin.html"}, method = RequestMethod.GET)
    public String getAdmin(Model model){

        List<Produto> produtoList = service.findAllNotDeleted();
        model.addAttribute("produtoList", produtoList);
        model.addAttribute("estiloUser", "estilos.css");
        return "admin.html";
    }

    @RequestMapping(value = {"/", "/index", "/index.html"}, method = RequestMethod.GET)
    public String getIndex(Model model, HttpServletRequest request, HttpServletResponse response){

        Cookie[] cookies = request.getCookies();
        boolean visitCookieExists = false;
        String lastAccess = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("lastAccess")) {
                    visitCookieExists = true;
                    lastAccess = cookie.getValue();
                    break;
                }
            }
        }

        if (!visitCookieExists) {

            LocalDateTime now = LocalDateTime.now();
            String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss"));
            formattedDateTime = formattedDateTime.replace(" ", "_");
            Cookie visitCookie = new Cookie("lastAccess", formattedDateTime);
            response.addCookie(visitCookie);
            lastAccess = formattedDateTime;
        }

        List<Produto> produtoList = service.findAllNotDeleted();
        model.addAttribute("produtoList", produtoList);
        model.addAttribute("estiloUser", "estilos.css");


        HttpSession session = request.getSession();
        List<Produto> carrinho = (List<Produto>) session.getAttribute("carrinho");
        int quantidadeProdutos = carrinho != null ? carrinho.size() : 0;
        model.addAttribute("quantidadeProdutos", quantidadeProdutos);

        model.addAttribute("lastAccess", lastAccess); 


       
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

       
        model.addAttribute("username", username);

        return "index.html";
    }


    @GetMapping("/cadastrarPage")
    public String getCadastrarPage(Model model){
        Produto p = new Produto();
        model.addAttribute("produto", p);
        return "cadastrarPage";
    }



    @Autowired
    private ProdutoRepository repo;
    @PostMapping("/doSalvar")
    public RedirectView saveProduto(@Valid @ModelAttribute("produto") Produto produto,BindingResult bindingResult,RedirectAttributes redirectAttributes, @RequestParam("image") MultipartFile multipartFile) throws IOException {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Erro ao editar o produto. Certifique-se de preencher todos os campos obrigatórios.");
            return new RedirectView("/admin", true);
        }else{
                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                produto.setImageURI(fileName);
                
                Produto savedProduto = repo.save(produto);
        
                String uploadDir = "user-photos/" + savedProduto.getId();
        
                Upload.saveFile(uploadDir, fileName, multipartFile);
                redirectAttributes.addFlashAttribute("success", "Produto editado com sucesso!");
                return new RedirectView("/admin", true);

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
    public String exibirCarrinho(Model model, HttpServletRequest request,RedirectAttributes redirectAttributes) {
    HttpSession session = request.getSession();
    List<Produto> carrinho = (List<Produto>) session.getAttribute("carrinho");

    if (carrinho == null || carrinho.isEmpty()) {
        redirectAttributes.addFlashAttribute("error", "O seu carrinho está vazio.");
        return "redirect:/index.html";
    } else {
        model.addAttribute("carrinho", carrinho);
        model.addAttribute("quantidadeProdutos", carrinho.size());
        return "carrinhoPage";
    }
    }

    @GetMapping("/finalizarCompra")
    public String finalizarCompra(HttpServletRequest request) {
    HttpSession session = request.getSession();

    session.invalidate();

    return "redirect:/index";
    }

    @GetMapping("/logout")
    public String doLogout(HttpServletRequest request) {
    HttpSession session = request.getSession();
    
    session.invalidate();
    return "redirect:/index";
    }




}



