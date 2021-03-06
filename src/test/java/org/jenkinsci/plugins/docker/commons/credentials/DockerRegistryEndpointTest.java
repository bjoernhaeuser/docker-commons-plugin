/*
 * The MIT License
 *
 * Copyright (c) 2015, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jenkinsci.plugins.docker.commons.credentials;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.jvnet.hudson.test.Issue;

/**
 * @author Carlos Sanchez <carlos@apache.org>
 */
public class DockerRegistryEndpointTest {

    @Test
    public void testParse() throws Exception {
        assertRegistry("https://index.docker.io/v1/", "acme/test");
        assertRegistry("https://index.docker.io/v1/", "busybox");
        assertRegistry("https://docker.acme.com:8080", "docker.acme.com:8080/acme/test");
        assertRegistry("https://docker.acme.com", "docker.acme.com/acme/test");
        assertRegistry("https://docker.acme.com", "docker.acme.com/busybox");
    }

    @Test
    public void testParseWithTags() throws Exception {
        assertRegistry("https://index.docker.io/v1/", "acme/test:tag");
        assertRegistry("https://index.docker.io/v1/", "busybox:tag");
        assertRegistry("https://docker.acme.com:8080", "docker.acme.com:8080/acme/test:tag");
        assertRegistry("https://docker.acme.com", "docker.acme.com/acme/test:tag");
        assertRegistry("https://docker.acme.com", "docker.acme.com/busybox:tag");
    }

    @Issue("JENKINS-39181")
    @Test
    public void testParseFullyQualifiedImageName() throws Exception {
        assertEquals("private-repo:5000/test-image", new DockerRegistryEndpoint("http://private-repo:5000/", null).imageName("private-repo:5000/test-image"));
        assertEquals("private-repo:5000/test-image", new DockerRegistryEndpoint("http://private-repo:5000/", null).imageName("test-image"));
    }

    @Issue("JENKINS-39181")
    @Test(expected = IllegalArgumentException.class)
    public void testParseNullImageName() throws Exception {
        new DockerRegistryEndpoint("http://private-repo:5000/", null).imageName(null);
    }

    @Issue("JENKINS-39181")
    @Test(expected = IllegalArgumentException.class)
    public void testParseNullUrlAndImageName() throws Exception {
        new DockerRegistryEndpoint(null, null).imageName(null);
    }

    private void assertRegistry(String url, String repo) throws IOException {
        assertEquals(url, DockerRegistryEndpoint.fromImageName(repo, null).getEffectiveUrl().toString());
    }

}
