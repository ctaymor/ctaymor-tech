# bisl-theme
A little theme for the [Hugo blogging framework](https://gohugo.io/), developed for my site tech.taymor.io. It is designed to host a basic portfolio site and blog.

What is a bisl? In Yiddish, [a bisl](http://www.jewish-languages.org/jewish-english-lexicon/words/67) is a little bit. Bisl-theme is a stylistically little Hugo theme, with minimalist design. 

## Warning:
Bisl is not currently stable, and may never be promised to be stable. It's something I'm building for personal use but am happy to share. If you use it, and want me to create stable releases, feel free to reach out and ask whether I'm able to support that.

Until then, assume that ANY update to bisl may contain breaking changes.

## To Install

```
$ cd <hugo repo root dir>
$ git submodule add https://github.com/ctaymor/bisl-theme.git themes/bisl
$ git submodule init
$ git submodule update
```

## Archetypes:
### Posts:

The frontmatter for your posts should include:
key | used for 
----|------------------------
title | The name of the blogpost. This will be used for the link, the top of the post, and also as the title for the webpage in the metadata, so best to keep it short and simple
date | The date you want the page to display as posted 
tags | any categories related to your blogpost. Currently unused
draft | false if post is ready for publication
showDate | true or false. If true, the blogpost date will be displayed. If not, the blogpost date will not be displayed anywhere. Use false for always relevant posts.

You can add an index page at `content/posts/_index.html` with an introduction and title for your list of blogposts if you wish.

### Talks:

The frontmatter for your talks should include:
key      		| used for
--------		|----------------------

title 			| The name of the talk
date 			| Not currently used, but will likely be used as the date the talk was GIVEN in the future
draft 			| false if post is ready for publication
location 		| The location the talk was given. For example, "RubyConf 2017". In future editions of Bisl we will likely handle the date through the date key, but for now, you may wish to include it with location
slidesLink 		| Optional link to slides, for example on Speaker Deck or Prezi
youtubeVideoId 	| Optional The ID code of the video. This can be found in the url, under the `?v=<string here>` query param. If provided, your video will be embeded.
videoLink 		| Optional A pretty link to the video, for instance at the conference's website or Confreaks. Used for the video link.

You might wish to include an _index.html file in `content/talks`. 
May we suggest something like this:

```
If you are interested in me giving one of the talks below or another talk on <subject>, please reach out to me via <contact method>.
```

### Contact Page
For the contact page on the index page, Bisl uses [Formspree](formspree.io). You will need an account on formspree to recieve messages.
