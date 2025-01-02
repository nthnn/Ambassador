/*
 * Copyright (c) 2024 - Nathanne Isip
 * This file is part of Ambassador.
 *
 * Ambassador is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Ambassador is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Ambassador. If not, see <https://www.gnu.org/licenses/>.
 */
package web.ambassador.view;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an HTML DOM (Document Object Model) element that can be dynamically constructed
 * and rendered as part of an HTTP response.
 *
 * The {@code Dom} class implements the {@code ViewContent} interface, allowing HTML
 * elements to be programmatically generated, modified, and serialized into an HTML string.
 * This class provides a flexible and fluent API to build complex HTML structures without
 * manually writing raw HTML tags.
 *
 * <p>Common use cases for this class include:</p>
 * <ul>
 *     <li>Generating HTML tables, forms, and component-based UIs.</li>
 *     <li>Creating dynamic server-rendered HTML pages.</li>
 *     <li>Embedding HTML fragments in responses for web applications.</li>
 * </ul>
 *
 * The {@code Dom} class supports the addition of children, attributes, CSS classes,
 * inline styles, and custom HTML content. Cached rendering optimizes performance by storing
 * the serialized byte array of the generated HTML.
 *
 * @see web.ambassador.view.ViewContent
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @since 1.0
 * @version 1.0
 */
public class Dom implements ViewContent {
    private byte[] cache = null;
    private String textContent;
    private final String tagName;
    private final List<String> classes;
    private final List<ViewContent> children;
    private final Map<String, String> styles;
    private final Map<String, String> attributes;

    /**
     * Constructs a DOM element with the specified tag name.
     *
     * @param tagName The HTML tag name (e.g., {@code "div"}, {@code "span"}).
     */
    public Dom(String tagName) {
        this.tagName = tagName;
        this.attributes = new HashMap<>();
        this.children = new ArrayList<>();
        this.styles = new HashMap<>();
        this.classes = new ArrayList<>();
    }

    /**
     * Constructs a DOM element with a tag name and inner text content.
     *
     * @param tagName     The HTML tag name.
     * @param textContent The inner text content of the element.
     */
    public Dom(String tagName, String textContent) {
        this(tagName);
        this.textContent = textContent;
    }

    /**
     * Constructs a DOM element with a tag name and child elements.
     *
     * @param tagName  The HTML tag name.
     * @param children Child elements to be nested within this element.
     */
    public Dom(String tagName, ViewContent... children) {
        this(tagName);
        this.children.addAll(Arrays.asList(children));
    }

    /**
     * Creates an HTML {@code !DOCTYPE} declaration with the root {@code html} tag.
     *
     * @return A {@code Dom} object representing an HTML page structure.
     */
    public static Dom createPage() {
        return new Dom("!DOCTYPE")
            .attr("html");
    }

    /**
     * Creates an HTML {@code !DOCTYPE} declaration with child elements nested
     * under the {@code html} tag.
     *
     * @param children Child elements to be added to the HTML page.
     * @return A {@code Dom} object representing an HTML page structure.
     */
    public static Dom createPage(ViewContent... children) {
        return new Dom("!DOCTYPE")
            .attr("html")
            .children(children);
    }

    public static Dom a() {
        return new Dom("a");
    }

    public static Dom a(String link, String text) {
        return new Dom("a", text)
            .href(link);
    }

    public static Dom a(String link, String text, String target) {
        return new Dom("a", text)
            .href(link)
            .attr("target", target);
    }

    public static Dom abbr(String text) {
        return new Dom("abbr", text);
    }

    public static Dom acronym(String text) {
        return new Dom("acronym", text);
    }

    public static Dom acronym(String text, String title) {
        return new Dom("acronym", text)
            .attr("title", title);
    }

    public static Dom address(String text) {
        return new Dom("address", text);
    }

    public static Dom address(String text, ViewContent... children) {
        return new Dom("address", text)
            .children(children);
    }

    public static Dom area() {
        return new Dom("area");
    }

    public static Dom area(String shape, String coords) {
        return new Dom("area")
            .attr("shape", shape)
            .attr("coords", coords);
    }

    public static Dom area(String shape, String coords, String href) {
        return new Dom("area")
            .attr("shape", shape)
            .attr("coords", coords)
            .href(href);
    }

    public static Dom article() {
        return new Dom("article");
    }

    public static Dom article(ViewContent... children) {
        return new Dom("article")
            .children(children);
    }

    public static Dom aside() {
        return new Dom("aside");
    }

    public static Dom aside(ViewContent... children) {
        return new Dom("aside")
            .children(children);
    }

    public static Dom audio() {
        return new Dom("audio");
    }

    public static Dom audio(String src) {
        return new Dom("audio")
            .attr("src", src);
    }

    public static Dom audio(ViewContent... children) {
        return new Dom("audio")
            .children(children);
    }

    public static Dom b() {
        return new Dom("b");
    }

    public static Dom b(String text) {
        return new Dom("b", text);
    }

    public static Dom b(Dom... children) {
        return new Dom("b")
            .children(children);
    }

    public static Dom base() {
        return new Dom("base");
    }

    public static Dom base(String href) {
        return new Dom("base")
            .href(href);
    }

    public static Dom base(String href, String target) {
        return new Dom("base")
            .href(href)
            .attr("target", target);
    }

    public static Dom bdi() {
        return new Dom("bdi");
    }

    public static Dom bdi(String text) {
        return new Dom("bdi", text);
    }

    public static Dom bdo() {
        return new Dom("bdo");
    }

    public static Dom bdo(String text) {
        return new Dom("bdo", text);
    }

    public static Dom bdo(String text, String dir) {
        return new Dom("bdo", text)
            .attr("dir", dir);
    }

    public static Dom blockquote() {
        return new Dom("blockquote");
    }

    public static Dom blockquote(String cite) {
        return new Dom("blockquote")
            .attr("cite", cite);
    }

    public static Dom blockquote(String cite, String text) {
        return new Dom("blockquote", text)
            .attr("cite", cite);
    }

    public static Dom blockquote(String cite, Dom... children) {
        return new Dom("blockquote")
            .children(children)
            .attr("cite", cite);
    }

    public static Dom body() {
        return new Dom("body");
    }

    public static Dom body(ViewContent... children) {
        return new Dom("body")
            .children(children);
    }

    public static Dom br() {
        return new Dom("br");
    }

    public static Dom button() {
        return new Dom("button");
    }

    public static Dom button(String text) {
        return new Dom("button", text);
    }

    public static Dom canvas() {
        return new Dom("canvas");
    }

    public static Dom canvas(String id) {
        return new Dom("canvas")
            .id(id);
    }

    public static Dom canvas(String id, int width, int height) {
        return new Dom("canvas")
            .attr("width", Integer.toString(width))
            .attr("height", Integer.toString(height))
            .id(id);
    }

    public static Dom caption() {
        return new Dom("caption");
    }

    public static Dom caption(String align, String text) {
        return new Dom("caption", text)
            .attr("align", align);
    }

    public static Dom caption(String align, Dom... children) {
        return new Dom("caption")
            .children(children)
            .attr("align", align);
    }

    public static Dom div() {
        return new Dom("div");
    }

    public static Dom div(ViewContent... children) {
        return new Dom("div")
            .children(children);
    }

    public static Dom h1() {
        return new Dom("h1");
    }

    public static Dom h1(String text) {
        return new Dom("h1", text);
    }

    public static Dom h1(ViewContent... children) {
        return new Dom("h1")
            .children(children);
    }

    public static Dom h2() {
        return new Dom("h2");
    }

    public static Dom h2(String text) {
        return new Dom("h2", text);
    }

    public static Dom h2(ViewContent... children) {
        return new Dom("h2")
            .children(children);
    }

    public static Dom h3() {
        return new Dom("h3");
    }

    public static Dom h3(String text) {
        return new Dom("h3", text);
    }

    public static Dom h3(ViewContent... children) {
        return new Dom("h3")
            .children(children);
    }

    public static Dom h4() {
        return new Dom("h4");
    }

    public static Dom h4(String text) {
        return new Dom("h4", text);
    }

    public static Dom h4(ViewContent... children) {
        return new Dom("h4")
            .children(children);
    }

    public static Dom h5() {
        return new Dom("h5");
    }

    public static Dom h5(String text) {
        return new Dom("h5", text);
    }

    public static Dom h5(ViewContent... children) {
        return new Dom("h5")
            .children(children);
    }

    public static Dom h6() {
        return new Dom("h6");
    }

    public static Dom h6(String text) {
        return new Dom("h6", text);
    }

    public static Dom h6(ViewContent... children) {
        return new Dom("h6")
            .children(children);
    }

    public static Dom head() {
        return new Dom("head");
    }

    public static Dom head(ViewContent... children) {
        return new Dom("head")
            .children(children);
    }

    public static Dom html(String lang) {
        return new Dom("html")
            .attr("lang", lang);
    }

    public static Dom html(ViewContent... children) {
        return new Dom("html", children);
    }

    public static Dom html(String lang, ViewContent... children) {
        return new Dom("html", children)
            .attr("lang", lang);
    }

    public static Dom html() {
        return new Dom("html");
    }

    public static Dom noTag(String text) {
        return new Dom(null)
            .text(text);
    }
    
    public static Dom p() {
        return new Dom("p");
    }

    public static Dom p(String text) {
        return new Dom("p", text);
    }

    public static Dom p(ViewContent... children) {
        return new Dom("p")
            .children(children);
    }

    public static Dom script() {
        return new Dom("script");
    }

    public static Dom script(String link) {
        return new Dom("script")
            .attr("src", link);
    }

    public static Dom script(String lang, String content) {
        return new Dom("script")
            .attr("type", "text/" + lang)
            .text(content);
    }

    public static Dom stylesheet(String path) {
        return new Dom("link")
            .attr("rel", "stylesheet")
            .href(path);
    }

    public static Dom title() {
        return new Dom("title");
    }

    public static Dom title(String text) {
        return new Dom("title", text);
    }

    public static Dom span() { return new Dom("span"); }
    public static Dom input() { return new Dom("input"); }
    public static Dom form() { return new Dom("form"); }
    public static Dom table() { return new Dom("table"); }
    public static Dom tr() { return new Dom("tr"); }
    public static Dom td() { return new Dom("td"); }
    public static Dom th() { return new Dom("th"); }
    public static Dom ul() { return new Dom("ul"); }
    public static Dom li() { return new Dom("li"); }
    public static Dom img() { return new Dom("img"); }

    public Dom id(String id) { return this.attr("id", id); }
    public Dom href(String url) { return this.attr("href", url); }
    public Dom src(String url) { return this.attr("src", url); }
    public Dom type(String type) { return this.attr("type", type); }
    public Dom value(String value) { return this.attr("value", value); }
    public Dom placeholder(String text) { return this.attr("placeholder", text); }
    public Dom name(String name) { return this.attr("name", name); }

    /**
     * Sets or replaces the inner text content of this element.
     *
     * @param text The new text content.
     * @return This {@code Dom} instance, for method chaining.
     */
    public Dom text(String text) {
        this.textContent = text;
        return this;
    }

    /**
     * Adds or updates an HTML attribute without a value (e.g., {@code checked}, {@code disabled}).
     *
     * @param name The name of the attribute.
     * @return This {@code Dom} instance.
     */
    public Dom attr(String name) {
        attributes.put(name, null);
        return this;
    }

    /**
     * Adds or updates an HTML attribute with a value.
     *
     * @param key   The attribute name.
     * @param value The attribute value.
     * @return This {@code Dom} instance.
     */
    public Dom attr(String key, String value) {
        attributes.put(key, value);
        return this;
    }

    /**
     * Applies inline CSS styles to the element.
     *
     * @param property The CSS property (e.g., {@code color}).
     * @param value    The value of the CSS property (e.g., {@code red}).
     * @return This {@code Dom} instance.
     */
    public Dom style(String property, String value) {
        styles.put(property, value);
        return this;
    }

    /**
     * Adds CSS classes to the element.
     *
     * @param classNames CSS class names to add.
     * @return This {@code Dom} instance.
     */
    public Dom addClass(String... classNames) {
        classes.addAll(Arrays.asList(classNames));
        return this;
    }

    /**
     * Removes CSS classes from the element.
     *
     * @param classNames CSS class names to remove.
     * @return This {@code Dom} instance.
     */
    public Dom removeClass(String... classNames) {
        classes.removeAll(Arrays.asList(classNames));
        return this;
    }

    public List<String> getClasses() {
        return this.classes;
    }

    public Dom addChild(Dom child) {
        children.add(child);
        return this;
    }

    public Dom removeChild(Dom child) {
        children.remove(child);
        return this;
    }

    public Dom children(ViewContent... elements) {
        children.addAll(Arrays.asList(elements));
        return this;
    }

    public List<ViewContent> getChildren() {
        return this.children;
    }

    public Dom addRow(String... cells) {
        Dom row = Dom.tr();
        for(String cell : cells)
            row.addChild(td().text(cell));

        return this.addChild(row);
    }

    public Dom addHeaderRow(String... headers) {
        Dom row = Dom.tr();
        for(String header : headers)
            row.addChild(th().text(header));

        return this.addChild(row);
    }

    /**
     * Renders the DOM element to a byte array in UTF-8 encoding.
     *
     * <p>The resulting byte array is cached for reuse to optimize performance.</p>
     *
     * @return A byte array representing the serialized HTML of this element.
     */
    @Override
    public byte[] render() {
        if(this.cache == null)
            this.cache = this.toString()
                .getBytes(StandardCharsets.UTF_8);

        return this.cache;
    }

    /**
     * Returns the MIME type for HTML content.
     *
     * @return {@code "text/html"}.
     */
    @Override
    public String mimeType() {
        return "text/html";
    }

    /**
     * Serializes the DOM element and its children to an HTML string.
     *
     * @return A string representing the HTML structure of this element.
     */
    @Override
    public String toString() {
        StringBuilder html = new StringBuilder();

        if(this.tagName != null) {
            html.append("<")
                .append(this.tagName);

            if(!this.classes.isEmpty())
                html.append(" class=\"")
                    .append(String.join(" ", this.classes))
                    .append("\"");

            if(!this.styles.isEmpty()) {
                html.append(" style=\"");
                this.styles.forEach((k, v) ->
                    html.append(k)
                        .append(":")
                        .append(v)
                        .append(";")
                );
                html.append("\"");
            }

            this.attributes.forEach((k, v) -> {
                if(v != null)
                    html.append(" ")
                        .append(k)
                        .append("=\"")
                        .append(v)
                        .append("\"");
                else html.append(" ")
                    .append(k);
            });

            if(this.tagName.equals("img") ||
                this.tagName.equals("link") ||
                this.tagName.equals("br") ||
                this.tagName.equals("base"))
                html.append(" />");
            else html.append(">");
        }

        if(this.textContent != null)
            html.append(this.textContent);

        for(ViewContent child : this.children)
            html.append(child);

        if(this.tagName != null &&
            !this.tagName.equals("!DOCTYPE") &&
            !this.tagName.equals("img") &&
            !this.tagName.equals("link") &&
            !this.tagName.equals("br") &&
            !this.tagName.equals("base"))
            html.append("</")
                .append(this.tagName)
                .append(">");

        return html.toString();
    }
}
