import osmnx as ox
import pandas as pd

ox.settings.timeout = 300
ox.settings.use_cache = True

# Descargar red viaria completa de Madrid
G = ox.graph_from_place(
    "Madrid, Community of Madrid, Spain",
    network_type="drive",
    simplify=True,
    retain_all=True
)

# Pasar a tablas
nodes, edges = ox.graph_to_gdfs(G)

# =========================
# NODOS
# =========================
nodes_df = nodes[["x", "y"]].copy()
nodes_df = nodes_df.rename(columns={"x": "lon", "y": "lat"})
nodes_df.index.name = "node_id"
nodes_df = nodes_df.reset_index()

nodes_df.to_csv("nodes.csv", index=False)

# =========================
# ARISTAS
# =========================
# u, v y key vienen en el índice, no en columnas
edges = edges.reset_index()

base_cols = ["u", "v", "length"]
optional_cols = ["name", "oneway", "highway", "maxspeed"]

existing_optional = [c for c in optional_cols if c in edges.columns]
edges_df = edges[base_cols + existing_optional].copy()

edges_df = edges_df.rename(columns={
    "u": "source",
    "v": "target"
})

# Convertir campos problemáticos a texto
for col in existing_optional:
    edges_df[col] = edges_df[col].astype(str)

edges_df.to_csv("edges.csv", index=False)

print("Hecho: nodes.csv y edges.csv")
print(f"Nodos: {len(nodes_df):,}")
print(f"Aristas: {len(edges_df):,}")