<#ftl output_format="HTML">

<#include "../include/imports.ftl">
<#include "macro/documentHeader.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/stickyNavTags.ftl">
<#include "macro/hubBox.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>


<#--Modifiers-->
<#assign noBorder = {"noBorder": true} />
<#assign noPadding = {"noPadding": true} />
<#assign noBackgroundCol = {"noBackgroundCol": true} />
<#assign sameStyle = {"sameStyle": true} />

<#--Document Data-->
<#assign document = {} />
<#assign document += {"title":"News and Events", "summary": {"content":"Find all the latest news and events related content."}, "bannercontrols":{"icon":{"original":"images/penpaper.svg"}}} />

<#--News data-->
<#assign document += {"newsData":[{}, {}]} />

<#--Blog data-->
<#assign document += {"blogData":[{}]} />

<#--Resource Bundle-->
<@hst.setBundle basename="rb.doctype.news-events-homepage"/>
<@fmt.message key="headers.news" var="latestNews"/>
<@fmt.message key="headers.blogs" var="latestBlogs"/>

<article class="article article--news-hub" aria-label="Document Header">
    <@documentHeader document 'news-events-homepage' '' '' '' '' false></@documentHeader>

<#--    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide">-->
<#--        <div class="local-header article-header article-header--with-icon">-->
<#--            <div class="grid-wrapper">-->
<#--                <div class="article-header__inner">-->
<#--                    <div class="grid-row">-->
<#--                        <div class="column--two-thirds column--reset">-->
<#--                            <h1 class="local-header__title" data-uipath="document.title">${data.title}</h1>-->
<#--                            <p class="article-header__subtitle">${data.summary}.</p>-->
<#--                        </div>-->
<#--                        <div class="column--one-third column--reset">-->
<#--                            <img src="<@hst.webfile path="images/penpaper.svg" fullyQualified=true/>" alt="News article">-->
<#--                        </div>-->
<#--                    </div>-->
<#--                </div>-->
<#--            </div>-->
<#--        </div>-->
<#--    </div>-->

    <div class="grid-wrapper grid-wrapper--article" aria-label="Document Content">
        <div class="grid-row">
            <div class="column column--one-quarter page-block page-block--sidebar">
                <div id="sticky-nav">
                    <#assign index = [] />
                    <#if document.newsData?has_content>
                        <#assign index += [latestNews] />
                    </#if>
                    <#if document.blogData?has_content>
                        <#assign index += [latestBlogs] />
                    </#if>

                    <#assign links = [] />

                    <#list index as i>
                        <#assign links += [{ "url": "#" + slugify(i), "title": i }] />
                    </#list>

                    <@stickyNavSections getStickySectionNavLinks({"sections": links})></@stickyNavSections>
                </div>
            </div>

            <div class="column column--three-quarters page-block page-block--main">
                <#if document.newsData?has_content>
                    <div class="article-section article-section--letter-group" id="${slugify(latestNews)}">
                        <h2>${latestNews}</h2>
                        <div class="grid-row">
                            <div class="column column--reset">
                                <div class="hub-box-list bottom-margin-20" id="${slugify(latestNews)}-list">
                                    <#list document.newsData as news>
                                        <#assign data = {"title": "HSCN Summit 2018", "date": "18 January 2018", "text": "Conveniently orchestrate user friendly models without revolutionary.", "imagesection": "EMPTY", "link": "#" } />
                                        <#assign data += noBorder + noBackgroundCol + noPadding />
                                        <@hubBox data ></@hubBox>
                                    </#list>
                                </div>
                            </div>
                        </div>
                        <div class="grid-row bottom-margin-20">
                            <a href="#"><@fmt.message key="label.all-news"/></a>
                        </div>
                    </div>
                </#if>

                <#if document.blogData?has_content>
                    <div class="article-section article-section--letter-group" id="${slugify(latestBlogs)}">
                        <h2>${latestBlogs}</h2>
                        <div class="grid-row">
                            <div class="column column--reset">
                                <div class="hub-box-list bottom-margin-20" id="${slugify(latestBlogs)}-list">
                                    <#list document.blogData as blog>
                                        <#assign data = {"title": "HSCN Summit 2018", "date": "18 January 2018", "text": "Conveniently orchestrate user friendly models without revolutionary.", "imagesection": "EMPTY", "link": "#" } />
                                        <#assign data += noBorder + noBackgroundCol + noPadding + sameStyle/>
                                        <@hubBox data ></@hubBox>
                                    </#list>
                                </div>
                            </div>
                        </div>
                        <div class="grid-row bottom-margin-20">
                            <a href="#"><@fmt.message key="label.all-blogs"/></a>
                        </div>
                    </div>
                </#if>
            </div>
        </div>
    </div>
</article>
