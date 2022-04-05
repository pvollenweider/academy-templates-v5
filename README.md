# Academy templates v5

A few search and replace are mandatory to migrate from bootstrap 3 to bootstrap 5.
Here is the list of search replace to do on the repository.xml (default + live)

- bootstrap3nt:columns -> bootstrap5nt:grid
- bootstrap5mix:customColumns -> bootstrap5mix:customGrid
- bootstrap5mix:predefinedColumns -> bootstrap5mix:predefinedGrid
- bootstrap5mix:siteLogo / siteLogo ->  bootstrap5mix:siteBrand / brandImage
- bootstrap5mix:advancedModal / size -> bootstrap5mix:modal -> modalSize
- bootstrap3nt:modal -> bootstrap5nt:button + add mixin bootstrap5mix:modal
- columnsType -> typeOfGrid
- typeOfGrid="predefinedColumns" -> typeOfGrid="predefinedGrid"
- typeOfGrid="customColumns" -> typeOfGrid="customGrid"
- gridLayout -> containerType
- fixed-width -> container
- full-width -> container-fluid
- fadeEffect -> fade
- navigation="tab" -> type="tab"
- navigation="pill" -> type="pill"
- useSystemNameAsAnchor -> useListNameAsAnchor
- tabsPosition="top" -> align="justify-content-start"

Also a few remove are mandatory

- remove navJustified="false"
- remove navStacked="false"
- remove state="primary"

And a few things to check:

- For lines with bootstrap5mix:customGrid check if there is a bootstrap5mix:createRow
- For lines with bootstrap5mix:predefinedGrid check if there is a bootstrap5mix:createRow
- For blocks with typeOfGrid="nogrid" check if there is a bootstrap5mix:createRow
- On jnt:virtualsite node, remove modal property

THen you can import

After that, you need to play 2 scripts:

- grid-refactoring.groovy
- grid-rename.groovy


This may be usefull if we remove the custom hidePage

Search for jacademix:hidePage
--> add j:displayInMenuName property with value hide-this-page

This can be done with this script

```
import org.jahia.api.Constants
import org.jahia.services.content.*
import org.jahia.services.sites.JahiaSite

import javax.jcr.NodeIterator
import javax.jcr.PathNotFoundException
import javax.jcr.RepositoryException
import javax.jcr.query.Query

String mixin = "jacademix:hidePage";
def JahiaSite site = org.jahia.services.sites.JahiaSitesService.getInstance().getSiteByKey("academy");

for (Locale locale : site.getLanguagesAsLocales()) {
    JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.EDIT_WORKSPACE, locale, new JCRCallback() {
        @Override
        Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
            def q = "SELECT * FROM [" + mixin + "]";

            NodeIterator iterator = session.getWorkspace().getQueryManager().createQuery(q, Query.JCR_SQL2).execute().getNodes();
            while (iterator.hasNext()) {
                final JCRNodeWrapper node = (JCRNodeWrapper) iterator.nextNode();
                node.addMixin("jmix:navMenu");
                JCRPropertyWrapper platformVersionsProperty = null;
                try {
                    platformVersionsProperty = node.getProperty("j:displayInMenuName");
                    try {
                        platformVersionsProperty.addValue("hide-this-page");
                    } catch (javax.jcr.ValueFormatException e) {
                    }
                    node.setProperty("j:displayInMenuName", platformVersionsProperty.getValues());
                    logger.info(node.path);
                    session.save();
                } catch (PathNotFoundException pnf) {

                }

            }
            return null;
        }
    }
    );
}

```










