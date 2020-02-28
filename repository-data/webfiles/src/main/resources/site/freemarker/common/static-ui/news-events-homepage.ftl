<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#include "../macro/stickyNavSections.ftl">
<#include "../macro/stickyNavTags.ftl">
<#include "../macro/hubBox.ftl">
<#include "../macro/stickyGroupBlockHeader.ftl">

<#assign latestNews="Latest news" />
<#assign features="Features" />
<#assign latestBlogs="Latest blog" />
<#assign resources="Resources" />

<#assign noBorder = {"noBorder": true} />
<#assign noPadding = {"noPadding": true} />
<#assign noBackgroundCol = {"noBackgroundCol": true} />
<#assign sameStyle = {"sameStyle": true} />

<article class="article article--news-hub" aria-label="Document Header">
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide">
        <div class="local-header article-header article-header--with-icon">
            <div class="grid-wrapper">
                <div class="article-header__inner">
                    <div class="grid-row">
                        <div class="column--two-thirds column--reset">
                            <h1 class="local-header__title" data-uipath="document.title">News and Events</h1>
                            <p class="article-header__subtitle">Find all the latest news and events related content.</p>
                        </div>
                        <div class="column--one-third column--reset">
                            <img src="<@hst.webfile path="images/penpaper.svg" fullyQualified=true/>" alt="News article">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="grid-wrapper grid-wrapper--article" aria-label="Document Content">
        <div class="grid-row">
            <div class="column column--one-quarter page-block page-block--sidebar">
                <div id="sticky-nav">
                    <#assign index = [latestNews, features, latestBlogs, resources] />

                    <#assign links = [] />

                    <#list index as i>
                        <#assign links += [{ "url": "#" + slugify(i), "title": i }] />
                    </#list>

                    <@stickyNavSections getStickySectionNavLinks({"sections": links})></@stickyNavSections>
                </div>
            </div>

            <div class="column column--three-quarters page-block page-block--main">
                <div class="article-section article-section--letter-group" id="${slugify(latestNews)}">
                    <h2>${latestNews}</h2>
                    <div class="grid-row">
                        <div class="column column--reset">
                            <div class="hub-box-list" id="${slugify(latestNews)}-list">
                                <#assign newsData = [{}, {}] />
                                <#list newsData as news>
                                    <@hst.webfile path="images/test-image.jpg" var="backgroundImage" />
                                    <#assign data = {"title": "HSCN Summit 2018", "date": "18 January 2018", "text": "Conveniently orchestrate user friendly models without revolutionary.", "imagesection": "EMPTY", "link": "#" } />
                                    <#assign data += noBorder + noBackgroundCol + noPadding />
                                    <@hubBox data ></@hubBox>
                                </#list>
                            </div>
                        </div>
                    </div>
                    <div class="grid-row" style="margin: 20px 0">
                        <a href="#">View all news</a>
                    </div>
                </div>

                <div class="article-section article-section--letter-group" id="${slugify(features)}">
                    <h2>${features}</h2>
                    <div class="grid-row">
                        <div class="column column--reset">
                            <div class="hub-box-list" id="${slugify(features)}-list">
                                <#assign newsData = [{}, {}] />
                                <#list newsData as news>
                                    <@hst.webfile path="images/test-image.jpg" var="backgroundImage" />
                                    <#assign data = {"title": "HSCN Summit 2018", "text": "Conveniently orchestrate user friendly models without revolutionary.", "imagesection": "EMPTY", "link": "#" } />
                                    <#assign data += noBorder + noBackgroundCol + noPadding + sameStyle/>
                                    <@hubBox data ></@hubBox>
                                </#list>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="article-section article-section--letter-group" id="${slugify(latestBlogs)}">
                    <h2>${latestBlogs}</h2>
                    <div class="grid-row">
                        <div class="column column--reset">
                            <div class="hub-box-list" id="${slugify(latestBlogs)}-list">
                                <#assign newsData = [{}] />
                                <#list newsData as news>
                                    <#assign data = {"title": "HSCN Summit 2018", "date": "18 January 2018", "text": "Conveniently orchestrate user friendly models without revolutionary.", "imagesection": "EMPTY", "link": "#" } />
                                    <#assign data += noBorder + noBackgroundCol + noPadding + sameStyle/>
                                    <@hubBox data ></@hubBox>
                                </#list>
                            </div>
                        </div>
                    </div>
                    <div class="grid-row" style="margin: 20px 0">
                        <a href="#">View all blogs</a>
                    </div>
                </div>

                <div class="article-section article-section--letter-group" id="${slugify(resources)}">
                    <h2>${resources}</h2>
                    <div class="grid-row">
                        <div class="column column--reset">
                            <div class="hub-box-list" id="${slugify(resources)}-list">
                                <#assign resources = [
                                {"title": "Upcoming events", "text": "Conveniently orchestrate user friendly models without revolutionary.", "link": "#" },
                                {"title": "Journalist and media resources", "text": "Conveniently orchestrate user friendly models without revolutionary.", "link": "#" },
                                {"title": "Supplementary information", "text": "Conveniently orchestrate user friendly models without revolutionary.", "link": "#" },
                                {"title": "NHS Digital style guidelines", "text": "Conveniently orchestrate user friendly models without revolutionary.", "link": "#" }
                                ] />
                                <#assign item = {} />
                                <#list resources as resource>
                                    <#assign item = resource />
                                    <#assign item += noBorder + noBackgroundCol + noPadding + sameStyle/>
                                    <@hubBox item ></@hubBox>
                                </#list>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</article>
