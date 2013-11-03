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

import com.granicus.grails.plugins.bugsnag.BugsnagExceptionResolver

class BugsnagGrailsPlugin {
    def version = "0.1"
    def grailsVersion = "2.0 > *"
    def title = "Grails Bugsnag Plugin"
    def author = "Ben Lucchesi"
    def authorEmail = "benlucchesi@gmail.com"
    def description = 'Integrates the bugsnag error reporting client and automatically reports exceptions to the bugsnag service.'
    def documentation = "http://grails.org/plugin/grails-bugsnag-plugin"

    def loadAfter = ['controllers']

    def license = "APACHE"
    def issueManagement = [ system: "GIT", url: "https://github.com/benlucchesi/grails-bugsnag-plugin" ]
    def scm = [ url: "https://github.com/benlucchesi/grails-bugsnag-plugin" ]

    def doWithSpring = {

        if ( !application.config.grails.plugin.bugsnag.enabled ) {
            return
        }

        exceptionHandler(BugsnagExceptionResolver){ bean ->
            bean.autowire = 'byName'
            exceptionMappings = ['java.lang.Exception': '/error']
        }
    }
}
