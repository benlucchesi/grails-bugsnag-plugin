/*
* Copyright 2013 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
*  Ben Lucchesi
*  ben@granicus.com or benlucchesi@gmail.com
*/

class BugsnagGrailsPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.1 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def title = "Grails Bugsnag Plugin" // Headline display name of the plugin
    def author = "Ben Lucchesi"
    def authorEmail = "benlucchesi@gmail.com"
    def description = '''\
      Integrates the bugsnag error reporting client into your grails application and automatically reports exceptions to the bugsnag service.
'''


  def loadAfter = ['controllers']

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/grails-bugsnag-plugin"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
//    def organization = [ name: "My Company", url: "http://www.my-company.com/" ]

    // Any additional developers beyond the author specified above.
//    def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

    // Location of the plugin's issue tracker.
    def issueManagement = [ system: "GIT", url: "https://github.com/benlucchesi/grails-bugsnag-plugin" ]

    // Online location of the plugin's browseable source code.
    def scm = [ url: "https://github.com/benlucchesi/grails-bugsnag-plugin" ]

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before
    }

    def doWithSpring = {

        if ( !application.config.grails.plugin.bugsnag.enabled ) 
            return

        exceptionHandler(com.granicus.grails.plugins.bugsnag.BugsnagExceptionResolver){ bean ->
          bean.autowire = 'byName'
          exceptionMappings = ['java.lang.Exception': '/error']
        }
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    def onShutdown = { event ->
        // TODO Implement code that is executed when the application shuts down (optional)
    }
}
