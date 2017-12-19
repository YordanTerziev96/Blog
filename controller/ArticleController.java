package softuniBlog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import softuniBlog.bindingModel.ArticleBindingModel;
import softuniBlog.entity.Article;
import softuniBlog.entity.User;
import softuniBlog.repository.ArticleRepository;
import softuniBlog.repository.UserRepository;

import java.util.List;

@Controller
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepo;

    @Autowired
    private UserRepository userRepo;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/articles/list")
    public String listArticles(Model model){
        List<Article> articles = articleRepo.findAll();
        model.addAttribute("articles", articles);
        model.addAttribute("view", "articles/list");

        return "base-layout";

    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/articles/create")
    public String createShowForm(Model model){

        model.addAttribute("view", "articles/create");
        return "base-layout";

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/articles/create")
    public String create(Model model, ArticleBindingModel articleFormData){

        try {

            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            User user = userRepo.findByEmail(userDetails.getUsername());

            Article article = new Article();
            article.setTitle(articleFormData.getTitle());
            article.setContent(articleFormData.getContent());
            article.setAuthor(user);

            articleRepo.saveAndFlush(article);

            return "redirect:/";
        }catch (Exception ex){
            model.addAttribute("errorMsg", "Cannot create article");
            model.addAttribute("view", "articles/create");
            return "base-layout";

        }

    }
    @GetMapping("/articles/{id}")
    public String details(Model model, @PathVariable Integer id){
        if(!this.articleRepo.exists(id)){
            return "redirect:/";
        }
        Article article = this.articleRepo.findOne(id);

        model.addAttribute("article", article);
        model.addAttribute("view","articles/details");

        return "base-layout";
    }
    @GetMapping("/articles/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String edit(@PathVariable Integer id, Model model){
        if(!this.articleRepo.exists(id)){
            return "redirect:/";
        }
        Article article = this.articleRepo.findOne(id);

        model.addAttribute("view","articles/edit");
        model.addAttribute("article", article);


        return "base-layout";
    }

    @PostMapping("/articles/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editProcess(@PathVariable Integer id, ArticleBindingModel articleBindingModel){
        if(!this.articleRepo.exists(id)){
            return "redirect:/";
        }
        Article article = articleRepo.findOne(id);

        article.setTitle(articleBindingModel.getTitle());
        article.setContent(articleBindingModel.getContent());

        this.articleRepo.saveAndFlush(article);

        return "redirect:/articles/" + article.getId();
    }
    @GetMapping("/articles/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(Model model, @PathVariable Integer id){
        if(!this.articleRepo.exists(id)){
            return "redirect:/";
        }
        Article article = this.articleRepo.findOne(id);

        model.addAttribute("view","articles/delete");
        model.addAttribute("article", article);

        return "base-layout";

    }
    @PostMapping("articles/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteProcess(Model model, @PathVariable Integer id){
        if(!this.articleRepo.exists(id)){
            return "redirect:/";
        }
        Article article = articleRepo.findOne(id);
        this.articleRepo.delete(article);

        return "redirect:/";
    }

}
