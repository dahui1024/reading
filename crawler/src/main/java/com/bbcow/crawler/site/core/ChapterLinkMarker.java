package com.bbcow.crawler.site.core;

import com.bbcow.service.mongo.entity.BookSiteChapter;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by adan on 2017/11/24.
 */
public class ChapterLinkMarker {
    public static LinkNode parseNode(Page page){
        LinkNode root = new LinkNode("root", 0);
        Document document = page.getHtml().getDocument();
        Elements aNodes = document.getElementsByTag("a");
        aNodes.forEach(aNode -> {
            String link = aNode.absUrl("href");
            String text = StringUtils.isBlank(aNode.ownText()) ? aNode.text() : aNode.ownText();
            if (StringUtils.isBlank(text)){
                text = aNode.attr("title");
            }

            try {
                String noProtocolUri = link.replaceAll("((^https://)|(^http://))", "");
                if (noProtocolUri.contains("#")){
                    // 移除锚点
                    noProtocolUri = noProtocolUri.replaceAll("([#][\\S]+)","");
                }
                String[] locations = noProtocolUri.split("/");
                LinkNode currentNode = root;
                // i = 1 默认从子目录开始
                for (int i = 0; i < locations.length; i++) {
                    if (locations.length ==0){
                        break;
                    }
                    LinkNode newChild = new LinkNode(text, locations[i], i);
                    int index = currentNode.children.indexOf(newChild);
                    if (index >= 0){
                        LinkNode sameExistedChild = currentNode.children.remove(index);
                        if (StringUtils.isNotBlank(text)){
                            sameExistedChild.text = text;
                        }
                        currentNode.addChild(sameExistedChild);
                        currentNode = sameExistedChild;
                    }else {
                        currentNode.addChild(newChild);
                        currentNode = newChild;
                    }
                }
            } catch (Exception e) {

                System.out.println(link);
            }
        });
        return root;
    }

    public static List<BookSiteChapter> parseLink(LinkNode node, String uri){
        List<BookSiteChapter> links = new LinkedList<>();

        LinkNode maxCountChild = node.maxCountChildNode();
        for (LinkNode child : node.children){
            if (maxCountChild == null){
                BookSiteChapter chapter = new BookSiteChapter();
                chapter.setName(child.text);
                chapter.setUrl(uri+"/"+child.value);
                links.add(chapter);
                continue;
            }

            if (child.count > 100 || (maxCountChild.count - child.count < 10)){
                links.addAll(parseLink(child, uri+"/"+child.value));
            }
        }
        return links;
    }
    
    public static class LinkNode{
        public String value;
        public String text;
        public int count;
        public int layer;
        List<LinkNode> children = new LinkedList<>();

        LinkNode(String text, int layer){
            this.text = text;
            this.layer = layer;
        }
        LinkNode(String text, String value, int layer){
            this.text = text;
            this.value = value;
            this.layer = layer;
        }

        void addChild(LinkNode linkNode){
            children.add(linkNode);
            count+=1;
        }

        LinkNode maxCountChildNode(){
            int max = 0;
            LinkNode maxNode = null;
            for (LinkNode child : children){
                if (child.count > max){
                    max = child.count;
                    maxNode = child;
                }
            }
            return maxNode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            LinkNode linkNode = (LinkNode) o;

            return value.equals(linkNode.value);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public String toString() {
            return value+": "+ text +children.toString()+"\r\n";
        }
    }
}
