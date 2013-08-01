---
layout: docu
title: About this documentation
---

## Tookit
The documentation pages you are looking at are implemented using the following components

- [Github Pages](http://pages.github.com) as the hosting provider
- [Jekyll](http://jekyllrb.com) as the static site generator
- [Github Flavored Markdown](https://help.github.com/articles/github-flavored-markdown) (regular [Markdown](http://daringfireball.net/projects/markdown/syntax) syntax with some extensions) as the source format for documents
- A [tiny piece of CoffeeScript]({{ site.baseurl }}/js/table_of_contents.coffee) to automatically generate the table of contents section from a document source

Since these components are all well-documented (except for the last one), I won't go into details concerning their mechanics. The interaction of Github Pages and Jekyll is described [here](https://help.github.com/articles/using-jekyll-with-pages) and [here](http://jekyllrb.com/docs/github-pages).
Instead, this document focuses on how to add, edit and test new documentation pages.

## Directory Structure
The files which make up this documentation are located in the [gh-pages](https://github.com/enwida/idpRepo/tree/gh-pages) branch of our IDP repository.
Since this is an orphaned branch, please do _not_ just "git checkout" it inside your main working copy of the repository whenever you want to add/edit files there. Make a new clone and check out the gh-pages branch there, instead.

The directory structure itself is based on the [Jekyll Directory Structure](http://jekyllrb.com/docs/structure) without posts and drafts:

```
├── _config.yml        | The main Jekyll Config
├── _layouts           | HTML Layouts
├── _site              | "Compile" target folder - do not edit things here!
├── css                | CSS files
├── documents          | Documents (in Markdown or HTML format)
├── img                | Images
├── index.html         | Landing page
├── js                 | Scripts
```

## Adding a new document
I assume you already have a Markdown (or HTML) file to add at this point.

### Front-matter
The first thing you want to do now is adding a [Jekyll Front-matter](http://jekyllrb.com/docs/frontmatter) ontop of your file.
The only required parameters are *layout* and *title*.
Please use the *docu* layout for documents.
The *title* parameter defines the page title of your document as well as the main heading. So it's best to not have an \<h1> heading (#-heading in Markdown) with the same name in the doucment itself.
A typical front-matter looks like this:

```yaml
---
layout: docu
title: Hello World
---
```

### Copy the file
In order to add the new document to the page, just copy the Markdown/HTML file (now containing a front-matter) into the *documents* directory.

### Linking
Afterwards, please add a link to it in the index.html file:

```html

<!-- [...] -->
<h1>Components</h1>
<ul>
  <li><a href="documents/meta.html">[Meta] About this documentation</a></li>
  <!-- [...] -->
  <!-- Add your document: -->
  <li><a href="documents/hello.html">Hello world!</a></li>
</ul>
<!-- [...] -->
```

Note that even if you put a .md file (e.g. hello.md) in the documents directory, you will have to reference it as an .html file (e.g. hello.html) here. This is necessary because Jekyll will compile Markdown files to HTML inside the *_site* directory.

## Testing
Before pushing a new document to the repository, you will most likely want to test/view it locally to see if it works as intended.
I assume you have successfully installed [Jekyll](http://jekyllrb.com) and are able to run the *jekyll* command from the terminal by now.

- Open a terminal
- Change to the root of the *gh-pages* branch (`cd /path/to/gh-pages`)
- Run `jekyll serve --watch --baseurl ''`
- Open a browser and navigate to `http://localhost:4000`

You should see the documentation pages now.
The `--watch` option causes Jekyll to recompile whenever you edit one of the document files.

## Publishing
If you are satisfied with the results, just commit and push your changes to the *gh-pages* branch.
After around 5-10 minutes you can see the changes our github page under [enwida.github.com/idpRepo](http://enwida.github.com/idpRepo).

