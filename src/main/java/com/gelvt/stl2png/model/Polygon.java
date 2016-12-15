package com.gelvt.stl2png.model;

import java.util.Arrays;

/**
 * 3D模型的三角面片
 * Created by elvin on 16-12-6.
 */
public class Polygon {
    private Vertex[] vertices;
    private Vertex normal;

    /**
     * 取所有顶点
     * @return 所有顶点
     */
    public Vertex[] getVertices() {
        return vertices;
    }

    /**
     * 设置顶点
     * @param vertices 所有的顶点
     */
    public void setVertices(Vertex[] vertices) {
        this.vertices = vertices;
    }

    /**
     * 获取法向量顶点
     * @return 法向量顶点
     */
    public Vertex getNormal() {
        return normal;
    }

    /**
     * 设置法向量顶点
     * @param normal 法向量顶点
     */
    public void setNormal(Vertex normal) {
        this.normal = normal;
    }

    @Override
    public String toString() {
        return "Polygon{" +
                "vertices=" + Arrays.toString(vertices) +
                ", normal=" + normal +
                '}';
    }
}
