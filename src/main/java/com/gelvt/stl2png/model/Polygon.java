package com.gelvt.stl2png.model;

/**
 * 3D模型的三角面片
 * Created by elvin on 16-12-6.
 */
public class Polygon {
    private Coordinate[] vertices;
    private Coordinate normal;

    /**
     * 取所有顶点
     * @return 所有顶点
     */
    public Coordinate[] getVertices() {
        return vertices;
    }

    /**
     * 设置顶点
     * @param vertices 所有的顶点
     */
    public void setVertices(Coordinate[] vertices) {
        this.vertices = vertices;
    }

    /**
     * 获取法向量顶点
     * @return 法向量顶点
     */
    public Coordinate getNormal() {
        return normal;
    }

    /**
     * 设置法向量顶点
     * @param normal 法向量顶点
     */
    public void setNormal(Coordinate normal) {
        this.normal = normal;
    }


}
