//package co.arctern.api.provider.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.rest.webmvc.BasePathAwareController;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import springfox.documentation.spring.web.plugins.DocumentationPluginsBootstrapper;
//
///**
// * swagger generation controller.
// */
//@BasePathAwareController
//public class SwaggerGenController {
//
//    @Autowired
//    private DocumentationPluginsBootstrapper documentationPluginsBootstrapper;
//
//    @GetMapping("/initialize/documentation")
//    @CrossOrigin
//    public ResponseEntity<String> initDocs() {
//        documentationPluginsBootstrapper.start();
//        return new ResponseEntity<String>("Success", HttpStatus.OK);
//    }
//}
//
