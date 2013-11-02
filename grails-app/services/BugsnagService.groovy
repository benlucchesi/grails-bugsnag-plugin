package com.granicus.grails.plugins.bugsnag;

import com.bugsnag.Client
import com.bugsnag.MetaData

import org.codehaus.groovy.grails.web.errors.GrailsWrappedRuntimeException
import org.springframework.beans.factory.InitializingBean
import org.codehaus.groovy.grails.commons.ConfigurationHolder as ch
import org.codehaus.groovy.grails.web.context.ServletContextHolder as SCH
import org.codehaus.groovy.grails.web.errors.ErrorsViewStackTracePrinter
import grails.util.Environment

class BugsnagService {

    def grailsApplication
    def exceptionHandler
    def grailsResourceLocator

    def getConfiguredClient(def context){

      log.info "getConfiguredClient()"
      
      // configure the client

      if( !grailsApplication.config.grails.plugin.bugsnag.enabled ){
        log.info "bugsnag plugin is not enabled. returning null."
        return null
      }

      if( !grailsApplication.config.grails.plugin.bugsnag.containsKey('apikey') ){
        log.error "grails.plugin.bugsnag.apikey not configured. assign your bugsnag api key with this configuration value."
        return null
      }

      // create the bugsnag client
      def client = new com.bugsnag.Client( grailsApplication.config.grails.plugin.bugsnag.apikey )

      // configure the release stage or set it to the current environment name
      if( grailsApplication.config.grails.plugin.bugsnag.containsKey('releasestage') ){
        client.setReleaseStage( grailsApplication.config.grails.plugin.bugsnag.releasestage )
      }
      else{
        client.setReleaseStage( Environment.current.name )
      }

      // configure the context of the client
      if( context ){
        client.setContext( context )
      }
      else if( grailsApplication.config.grails.plugin.bugsnag.containsKey('context') ){
        client.setContext( grailsApplication.config.grails.plugin.bugsnag.context )
      }

      // set the application version
      client.setAppVersion( grailsApplication.metadata.'app.version' )

      return client
    }

    def notify(def request, def exception) {

      def client = getConfiguredClient(request.requestURI) 

      // get the current user
      // get the session
      // get the request uri
      // get the forward uri
      // get the request cookies

      try{
        client.setUserId(request.remoteUser)
        MetaData metaData = new MetaData();
  
        metaData.addToTab( "app", "grails version", grailsApplication.metadata.'app.grails.version' ) 
        metaData.addToTab( "app", "application name", grailsApplication.metadata.'app.name' ) 
  
        println "User principal: ${request.userPrincipal}"
        metaData.addToTab( "user", "remoteUser", request.remoteUser?:"(none)" )
        metaData.addToTab( "user", "userPrincipal", request.userPrincipal?:"(none)" )
  
        metaData.addToTab( "request", "requestURI", request.requestURI );
        metaData.addToTab( "request", "forwardURI", request.forwardURI) ;
        metaData.addToTab( "request", "cookies", request.cookies.collect{ "\nName: ${it.name}\nMax Age: ${it.maxAge}\nPath: ${it.path}\nSecure: ${it.secure}\nDomain: ${it.domain}\nVersion: ${it.version}\nValue: ${it.value}" }.join("\n") )
        metaData.addToTab( "request", "headers", request.headerNames.findAll{ it != 'cookie' }.collect{ headerName -> "${headerName}: ${request.getHeaders(headerName).toList()}" }.join('\n') )
        metaData.addToTab( "request", "authType", request.authType )
        metaData.addToTab( "request", "method", request.method )
        metaData.addToTab( "request", "server", request.serverName?:"(none)" )
        metaData.addToTab( "request", "port", request.serverPort?:"(none)" )
        metaData.addToTab( "request", "content type", request.contentType?:"(none)" )
        metaData.addToTab( "request", "character encoding", request.characterEncoding?:"(none)" )
        metaData.addToTab( "request", "scheme", request.scheme?:"(none)" )
        metaData.addToTab( "request", "queryString", request.queryString?:"(none)" )
  
        metaData.addToTab( "request", "xml", request.xml?.text() )
        metaData.addToTab( "request", "json", request.json?.text() )

        //TODO: get handler for including user defined metadata

        client.notify(exception,metaData)

      }catch( excp ){
        log.error "error calling notify", excp
      }
    }
}
